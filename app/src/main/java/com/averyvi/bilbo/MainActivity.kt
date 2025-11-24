package com.averyvi.bilbo

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.averyvi.bilbo.notui.FirstHarmonic
import com.averyvi.bilbo.notui.SelectableBluetoothDevice
import com.averyvi.bilbo.ui.theme.BilboTheme
import java.util.Locale
import java.util.UUID


class MainActivity : ComponentActivity() {
    lateinit var bluetoothManager: BluetoothManager
    var bluetoothAdapter: BluetoothAdapter? = null
    val bleScanner by lazy { bluetoothAdapter?.bluetoothLeScanner }
    var bluetoothGATT: BluetoothGatt? = null

    var isScanning = false
    val discoveredDevices = mutableStateListOf<SelectableBluetoothDevice>()
    val scanTimeoutHandler = Handler(Looper.getMainLooper())

    val connectionAttemptHandler = Handler(Looper.getMainLooper())
    var connectionAttempts = 0
    var connectionAttemptDevice: BluetoothDevice? = null
    val maxConnectionAttempts = 3
    val baseConnectionAttemptDelayMs = 1000L
    val maxConnectionAttemptDelayMs = 5000L

    var writtenGATTCharacteristic: BluetoothGattCharacteristic? = null
    var connectedGATT: BluetoothGatt? = null

    val TRANSPARENT_UART_SERVICE_UUID = "49535343-fe7d-4ae5-8fa9-9fafd205e455"
    val UART_TX_CHARACTERISTIC_UUID = "49535343-1e4d-4bd9-ba61-23c647249616" // Write coming from device
    val UART_RX_CHARACTERISTIC_UUID = "49535343-8841-43f4-a8d4-ecbe34729bb3" // Write coming from phone
    val CCCD_UUID = "00002902-0000-1000-8000-00805f9b34fb"

    var uartTxCharacteristic: BluetoothGattCharacteristic? = null // write target

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        enableEdgeToEdge()
        beginBluetooth()
        setContent {
            BilboTheme {
                AppUI(
                    deviceList = discoveredDevices,
                    onDeviceSelected = { selectedDevice ->
                        if (selectedDevice.isConnected) {
                            clearConnectionAttempts()
                            disconnectGATT(selectedDevice.device.address)
                        } else {
                            connectToDevice(selectedDevice.device)
                        }
                    },
                    onHarmonicSelected = { firstHarmonic ->
                        FrequencySelectionHandler(firstHarmonic)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (isScanning) {
            stopBTScan()
        }
        //bluetoothGatt?.let { disconnectGatt(it.device.address) }
        //clearPendingConnectionRetries()
    }

    /*permissions**********************************************************************************************/

    /**
     * <h3>function</h3>
     *
     * Checks if the necessary permissions for bluetooth are enabled and if not, then it enables them.
     */
    fun checkEnablePermissions(): Boolean{
        // check if the device has bt and if it is enabled
        val permissionsNeeded = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        val permissionsNotGranted = permissionsNeeded.filterNot { (ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED) }

        if(permissionsNotGranted.isEmpty()){
            Log.d("Permissions", "All necessary permissions already granted.")
            return true
        } else{
            Log.d("Permissions", "Requesting necessary permissions.")
            Log.d("Permissions", "Requesting:")
            Log.d("Permissions", permissionsNotGranted.joinToString("; "))
            requestPermissions.launch(permissionsNeeded)
            return requestPermissionsReturn.value?:false
        }
    }

    var requestPermissionsReturn: MutableState<Boolean?> = mutableStateOf(null)
    /**
     * <h3>lambda function</h3>
     *
     * Requests for the passed in permissions from the user.
     *
     * Usage
     * ```kt
     * requestPermissions.launch(permissionsNeeded)
     * ```
     */
    val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.all { it.value }
            if (allPermissionsGranted) {
                Log.d("Permissions", "All permissions granted.")
                requestPermissionsReturn = mutableStateOf(true)
            } else {
                Log.w("Permissions", "User denied one or more permissions. Cannot perform BLE operations.")
                requestPermissionsReturn = mutableStateOf(false)
            }
        }

    var enablePermissionReturn: MutableState<Boolean?> = mutableStateOf(null)
    /**
     * <h3>lambda function</h3>
     *
     * Asks the user for permission to enable a permission. If allowed, then starts scanning.
     *
     * Usage
     * ```kt
     * requestPermissions.launch(permissionsNeeded)
     * ```
     */
    val enablePermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Permissions", "Bluetooth enabled by user.")
                Log.d("BluetoothBegin", "Scanning begins.")
                enablePermissionReturn = mutableStateOf(true)
                startSBTScan()

            } else {
                Log.d("Permissions", "User denied Bluetooth enabling.")
                enablePermissionReturn = mutableStateOf(false)
            }
        }

    /*begin bluetooth**********************************************************************************************/

    fun beginBluetooth() {
        Log.d("Permissions", "Checking for necessary permissions.")
        if(checkEnablePermissions()){
            // proceed
            if (bluetoothAdapter?.isEnabled == false) {
                Log.d("BluetoothBegin", "Bluetooth disabled, asking user to enable it.")
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enablePermission.launch(enableIntent)
                if(enablePermissionReturn.value?:false){
                    Log.d("Permissions", "Bluetooth is disabled.")
                }
            } else if (bluetoothAdapter != null) {
                Log.d("BluetoothBegin", "Scanning begins.")
                startSBTScan()
            }
        } else{
            Log.d("Permissions", "App does not have the permissions for bluetooth")
        }
    }

    /*bluetooth scanning**********************************************************************************************/

    fun startSBTScan(){
        val btPermission = Manifest.permission.BLUETOOTH_SCAN
        if (ActivityCompat.checkSelfPermission(this, btPermission) != PackageManager.PERMISSION_GRANTED) return
        if (isScanning) return
        discoveredDevices.clear()
        isScanning = true

        scanTimeoutHandler.postDelayed({ stopBTScan() }, 10000)
        bleScanner?.startScan(scanCallback)
        Log.d("BluetoothScan", "Bluetooth scanning started.")
    }

    fun stopBTScan() {
        val btPermission = Manifest.permission.BLUETOOTH_SCAN
        if (ActivityCompat.checkSelfPermission(this, btPermission) != PackageManager.PERMISSION_GRANTED) return
        if (isScanning && (bluetoothAdapter?.isEnabled == true)) {
            bleScanner?.stopScan(scanCallback)
            isScanning = false
            scanTimeoutHandler.removeCallbacksAndMessages(null)
            Log.d("BluetoothScan", "BLE scan stopped.")
        }
    }

    val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.w("BluetoothScan", "BLUETOOTH_CONNECT permission missing")
                return
            }
            val device = result.device
            if (device != null && device.name != null && discoveredDevices.none { it.device.address == device.address }) {
                Log.d("BluetoothScan", "Found BLE device: ${device.name} - ${device.address}")
                discoveredDevices.add(SelectableBluetoothDevice(device))
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BluetoothScan", "BLE Scan failed with error code: $errorCode")
            isScanning = false
        }
    }

    /*bluetooth filters**********************************************************************************************/



    /*bluetooth connection**********************************************************************************************/

    fun connectToDevice(device: BluetoothDevice) {
        val btPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ActivityCompat.checkSelfPermission(this, btPermission) != PackageManager.PERMISSION_GRANTED) return

        clearConnectionAttempts()
        connectionAttemptDevice = device

        if(isScanning) stopBTScan()

        if(bluetoothGATT == null){
            establishConnection(device)
        } else {
            Log.d("BLE connect", "Existing GATT connection found. Disconnecting, before making a new connection")
            disconnectGATT(bluetoothGATT!!.device.address)
            Handler(Looper.getMainLooper()).postDelayed({
                establishConnection(device)
            }, 500)
        }
    }

    fun establishConnection(device: BluetoothDevice){
        val btPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ActivityCompat.checkSelfPermission(this, btPermission) != PackageManager.PERMISSION_GRANTED) return

        Log.d("BLE connect", "Attempting to connect to device: ${device.name} (${device.address})")

        Handler(Looper.getMainLooper()).post {
            discoveredDevices.forEachIndexed { index, selectable ->
                discoveredDevices[index] = selectable.copy(isSelected = selectable.device.address == device.address)
            }
            bluetoothGATT?.let { existingGATT ->
                closeGATTInstance(existingGATT)
                bluetoothGATT = null
                writtenGATTCharacteristic = null
            }
            bluetoothGATT = device.connectGatt(applicationContext, false, GATTCallback)
        }
    }

    fun updateDeviceConnectionState(deviceAddress: String, isConnected: Boolean) {
        val index = discoveredDevices.indexOfFirst { it.device.address == deviceAddress }
        if (index != -1) {
            val device = discoveredDevices[index]
            discoveredDevices[index] = device.copy(
                isConnected = isConnected,
                isSelected = device.isSelected || isConnected
            )
        }
    }

    /*GATTCallback**********************************************************************************************/

    val GATTCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val btPermission = Manifest.permission.BLUETOOTH_CONNECT
            if (ActivityCompat.checkSelfPermission(this@MainActivity, btPermission) != PackageManager.PERMISSION_GRANTED) {
                Log.w("GATT", "Missing permission BLUETOOTH_CONNECT")
                return
            }

            val deviceAddress = gatt.device.address
            Log.d("GattCallback","onConnectionStateChange address=$deviceAddress status=$status newState=$newState")

            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.w("GattCallback", "Non-success status $status for $deviceAddress")
                if (status == 133) {
                    Log.e("GattCallback","GATT_ERROR 133. Scheduling a reconnection attempt after a short delay.")
                }
                closeGATTInstance(gatt)
                if (connectedGATT == gatt) {
                    connectedGATT = null
                    writtenGATTCharacteristic = null
                }
                disconnectGATT(deviceAddress)
                if (status == 133) {
                    scheduleGattRetry(gatt.device)
                }
                return
            }

            when(newState){
                BluetoothProfile.STATE_CONNECTED -> {
                    bluetoothGATT = gatt
                    Log.i("GATT", "Connected to GATT server.")
                    clearConnectionAttempts()
                    Handler(Looper.getMainLooper()).post {
                        updateDeviceConnectionState(deviceAddress, isConnected = true)
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        val btPermission = Manifest.permission.BLUETOOTH_CONNECT
                        if (ActivityCompat.checkSelfPermission(this@MainActivity, btPermission) == PackageManager.PERMISSION_GRANTED){
                            gatt.discoverServices()
                        }
                    }, 600)
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.i("GattCallback", "Disconnected from GATT server.")
                    closeGATTInstance(gatt)
                    if (connectedGATT == gatt) {
                        connectedGATT = null
                        writtenGATTCharacteristic = null
                    }
                    disconnectGATT(deviceAddress)
                }
                else -> Log.d("GattCallback", "Unhandled state transition: $newState")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("GattCallback", "Successfully discovered device services.")
                val service = gatt.getService(UUID.fromString(TRANSPARENT_UART_SERVICE_UUID))
                if (service == null) {
                    Log.w("GattCallback", "Trans. UART Service was not found")
                } else {
                    Log.i("GattCallback", "Trans. UART Service was found")
                    val rxCharacteristic = service.getCharacteristic(UUID.fromString(UART_TX_CHARACTERISTIC_UUID))
                    uartTxCharacteristic = service.getCharacteristic(UUID.fromString(UART_RX_CHARACTERISTIC_UUID))

                    if (rxCharacteristic != null && uartTxCharacteristic != null) {
                        enableNotifications(gatt, rxCharacteristic)
                    } else {
                        Log.w("GattCallback", "Both UART characteristic not found")
                    }
                }
            } else {
                Log.w("GattCallback", "Services discovery received error: $status")
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("GattCallback", "Descriptor for ${descriptor.characteristic.uuid} written successfully. Notifications enabled.")
                // sendData("Hello from Android\r\n") // volitelné pro debug
            } else {
                Log.e("GattCallback", "Descriptor write failed with status: $status")
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            val dataString = value.toString(Charsets.UTF_8)
            Log.i("GattCallback", "Received data on ${characteristic.uuid}: $dataString")
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("GattCallback", "Wrote to ${characteristic.uuid}: ${characteristic.value?.toString(Charsets.UTF_8)}")
            } else {
                Log.e("GattCallback", "Characteristic write failed for ${characteristic.uuid} with status $status")
            }
        }
    }

    /*GATT other**********************************************************************************************/

    fun disconnectGATT(deviceAddress: String) {
        val btPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ActivityCompat.checkSelfPermission(this, btPermission) != PackageManager.PERMISSION_GRANTED) return

        Handler(Looper.getMainLooper()).post {
            updateDeviceConnectionState(deviceAddress, isConnected = false)
        }

        if (connectedGATT?.device?.address == deviceAddress) {
            connectedGATT?.let { closeGATTInstance(it) }
            connectedGATT = null
            uartTxCharacteristic = null
            Log.d("GATT", "GATT connection to $deviceAddress closed.")
        }
    }

    fun closeGATTInstance(gatt: BluetoothGatt) {
        try {
            val btPermission = Manifest.permission.BLUETOOTH_CONNECT
            if (ContextCompat.checkSelfPermission(this, btPermission) == PackageManager.PERMISSION_GRANTED) {
                gatt.disconnect()
            }
        } catch (disconnectError: Exception) {
            Log.w("GATT", "Error while disconnecting GATT", disconnectError)
        }

        try {
            gatt.close()
        } catch (closeError: Exception) {
            Log.w("GATT", "Error while closing GATT", closeError)
        }
    }

    fun scheduleGattRetry(device: BluetoothDevice) {
        val targetAddress = connectionAttemptDevice?.address
        if (targetAddress != null && targetAddress != device.address) {
            Log.d("GATT","Skipping retry because another device ($targetAddress) is selected.")
            return
        }

        if (connectionAttemptDevice == null) connectionAttemptDevice = device

        if (connectionAttempts >= maxConnectionAttempts) {
            Log.e("GATT","Reached maximum retry attempts for ${device.address}.")
            connectionAttemptDevice = null
            connectionAttempts = 0
            return
        }

        connectionAttempts += 1
        val retryDelay = (baseConnectionAttemptDelayMs * connectionAttempts).coerceAtMost(maxConnectionAttemptDelayMs)
        Log.i("GATT","Retry attempt $connectionAttempts for ${device.address} in ${retryDelay}ms.")

        connectionAttemptHandler.postDelayed({
            val btPermission = Manifest.permission.BLUETOOTH_CONNECT
            if (ActivityCompat.checkSelfPermission(this, btPermission) != PackageManager.PERMISSION_GRANTED) return@postDelayed
            connectedGATT?.let { existingGatt ->
                if (existingGatt.device.address == device.address) {
                    closeGATTInstance(existingGatt)
                    connectedGATT = null
                    uartTxCharacteristic = null
                }
            }
            establishConnection(device)
        }, retryDelay)
    }

    fun enableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        val btPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ActivityCompat.checkSelfPermission(this, btPermission) != PackageManager.PERMISSION_GRANTED) return

        val cccdUuid = UUID.fromString(CCCD_UUID)
        val descriptor = characteristic.getDescriptor(cccdUuid)
            ?: run {
                Log.e("GATT", "CCCD descriptor not found for characteristic ${characteristic.uuid}")
                return
            }

        gatt.setCharacteristicNotification(characteristic, true)

        val value = when {
            (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0 ->
                BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0 ->
                BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            else -> return
        }

        gatt.writeDescriptor(descriptor, value)
    }

    /*Bluetooth Communication**********************************************************************************************/

    fun sendData(data: String) {
        val btPermission = Manifest.permission.BLUETOOTH_CONNECT
        if (ActivityCompat.checkSelfPermission(this, btPermission) != PackageManager.PERMISSION_GRANTED) return

        val characteristic = uartTxCharacteristic ?: run {
            Log.w("SendData", "UART TX Characteristic not found, cannot send data.")
            return
        }

        val props = characteristic.properties
        characteristic.writeType =
            if (props and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0)
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
            else
                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT

        val dataBytes = data.toByteArray(Charsets.UTF_8)
        Log.d("SendData", "Sending data to BLE device: $data")

        connectedGATT?.writeCharacteristic(characteristic, dataBytes, characteristic.writeType)
    }

    /*misc**********************************************************************************************/

    fun clearConnectionAttempts() {
        connectionAttemptHandler.removeCallbacksAndMessages(null)
        connectionAttempts = 0
        connectionAttemptDevice = null
    }

    private fun FrequencySelectionHandler(firstHarmonic: FirstHarmonic) {
        if (connectedGATT != null && uartTxCharacteristic != null) {
            val frequencyPayload = String.format(Locale.US, "%.2f\r\n", firstHarmonic.frequency)
            sendData(frequencyPayload)
        } else {
            Log.w("MyActivity","Cannot send frequency ${firstHarmonic.frequency} Hz - no active BLE connection.")
        }
    }
}

/******************************************************************************************************************************************/
/******************************************************************************************************************************************/
