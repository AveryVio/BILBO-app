package com.averyvi.bilbo.comms

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.averyvi.bilbo.notui.SelectableBluetoothDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

@SuppressLint("MissingPermission") // Permissions are checked before calling these methods
class BilboBluetoothManager(private val context: Context) {

    // --- Variables ---

    private val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter = bluetoothManager?.adapter
    private val bleScanner get() = bluetoothAdapter?.bluetoothLeScanner

    // StateFlows for the ViewModel to observe
    private val _discoveredDevices = MutableStateFlow<List<SelectableBluetoothDevice>>(emptyList())
    val discoveredDevices = _discoveredDevices.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    // Internal State
    private var bluetoothGATT: BluetoothGatt? = null
    private var connectedGATT: BluetoothGatt? = null
    private var uartTxCharacteristic: BluetoothGattCharacteristic? = null
    private var isScanning = false

    // UUIDs
    private val TRANSPARENT_UART_SERVICE_UUID = "49535343-fe7d-4ae5-8fa9-9fafd205e455"
    private val UART_TX_CHARACTERISTIC_UUID = "49535343-1e4d-4bd9-ba61-23c647249616"
    private val UART_RX_CHARACTERISTIC_UUID = "49535343-8841-43f4-a8d4-ecbe34729bb3"
    private val CCCD_UUID = "00002902-0000-1000-8000-00805f9b34fb"

    // Handlers
    private val scanTimeoutHandler = Handler(Looper.getMainLooper())
    private val connectionAttemptHandler = Handler(Looper.getMainLooper())
    private var connectionAttempts = 0
    private var connectionAttemptDevice: BluetoothDevice? = null
    private val maxConnectionAttempts = 3
    private val baseConnectionAttemptDelayMs = 1000L
    private val maxConnectionAttemptDelayMs = 5000L

    // --- Scanning ---

    fun startScan() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) return
        if (isScanning) return

        _discoveredDevices.value = emptyList()
        isScanning = true
        scanTimeoutHandler.postDelayed({ stopScan() }, 10000)
        bleScanner?.startScan(scanCallback)
        Log.d("BilboBT", "Bluetooth scanning started.")
    }

    fun stopScan() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) return
        if (isScanning && bluetoothAdapter?.isEnabled == true) {
            bleScanner?.stopScan(scanCallback)
            isScanning = false
            scanTimeoutHandler.removeCallbacksAndMessages(null)
            Log.d("BilboBT", "BLE scan stopped.")
        }
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val currentList = _discoveredDevices.value
            if (device?.name != null && currentList.none { it.device.address == device.address }) {
                Log.d("BluetoothScan", "Found BLE device: ${device.name} - ${device.address}")
                _discoveredDevices.update { it + SelectableBluetoothDevice(device) }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BluetoothScan", "BLE Scan failed with error code: $errorCode")
            isScanning = false
        }
    }

    // --- Connection ---

    fun connectToDevice(device: BluetoothDevice) {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return

        clearConnectionAttempts()
        connectionAttemptDevice = device
        if (isScanning) stopScan()

        if (bluetoothGATT == null) {
            establishConnection(device)
        } else {
            Log.d("BLE connect", "Existing GATT connection found. Disconnecting, before making a new connection")
            disconnectGATT(bluetoothGATT!!.device.address)
            Handler(Looper.getMainLooper()).postDelayed({
                establishConnection(device)
            }, 500)
        }
    }

    private fun establishConnection(device: BluetoothDevice) {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return
        Log.d("BilboBT", "Connecting to: ${device.name} (${device.address})")

        // Update UI selection state locally
        _discoveredDevices.update { list ->
            list.map { it.copy(isSelected = it.device.address == device.address) }
        }

        bluetoothGATT?.let { closeGATTInstance(it) }
        bluetoothGATT = device.connectGatt(context, false, gattCallback)
    }

    fun disconnectGATT(deviceAddress: String) {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return
        updateDeviceConnectionState(deviceAddress, false)

        if (connectedGATT?.device?.address == deviceAddress) {
            connectedGATT?.let { closeGATTInstance(it) }
            connectedGATT = null
            uartTxCharacteristic = null
            _isConnected.value = false
            Log.d("GATT", "GATT connection to $deviceAddress closed.")
        }
    }

    private fun closeGATTInstance(gatt: BluetoothGatt) {
        if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            try { gatt.disconnect() } catch (e: Exception) { Log.w("BilboBT", "Error while disconnecting GATT", e) }
            try { gatt.close() } catch (e: Exception) { Log.w("BilboBT", "Error while closing GATT", e) }
        }
    }

    // --- GATT Callback ---

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
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
                    _isConnected.value = false
                }
                if (status == 133) scheduleGattRetry(gatt.device)
                return
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothGATT = gatt
                Log.i("GATT", "Connected to GATT server.")
                _isConnected.value = true
                clearConnectionAttempts()
                updateDeviceConnectionState(deviceAddress, true)

                Handler(Looper.getMainLooper()).postDelayed({
                    if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) gatt.discoverServices()
                }, 600)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("GattCallback", "Disconnected from GATT server.")
                closeGATTInstance(gatt)
                _isConnected.value = false
                updateDeviceConnectionState(deviceAddress, false)
            }
            else Log.d("GattCallback", "Unhandled state transition: $newState")
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("GattCallback", "Successfully discovered device services.")
                val service = gatt.getService(UUID.fromString(TRANSPARENT_UART_SERVICE_UUID))
                if (service != null) {
                    Log.i("GattCallback", "Trans. UART Service was found")
                    val rxChar = service.getCharacteristic(UUID.fromString(UART_TX_CHARACTERISTIC_UUID))
                    uartTxCharacteristic = service.getCharacteristic(UUID.fromString(UART_RX_CHARACTERISTIC_UUID))
                    if (rxChar != null) enableNotifications(gatt, rxChar)
                    else  Log.w("GattCallback", "RX characteristic not found")
                    if (uartTxCharacteristic == null)  Log.w("GattCallback", "TX characteristic not found")
                }
                else Log.w("GattCallback", "Trans. UART Service was not found")
            } else {
                Log.w("GattCallback", "Services discovery received error: $status")
            }
        }

        // triggered on data sending
        // TODO: IMPLEMENT
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

        // triggered on data reception
        // TODO: IMPLEMENT
        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("GattCallback", "Wrote to ${characteristic.uuid}")
            } else {
                Log.e("GattCallback", "Characteristic write failed for ${characteristic.uuid} with status $status")
            }
        }
    }

    // --- Data Transmission ---

    fun sendData(data: String) {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return
        val char = uartTxCharacteristic ?: run {
            Log.w("SendData", "UART TX Characteristic not found, cannot send data.")
            return
        }

        char.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        val dataBytes = data.toByteArray(Charsets.UTF_8)
        Log.d("SendData", "Sending data to BLE device: $data")

        // Note: writeCharacteristic signature depends on API level.
        // This is the modern way, for older APIs use char.value = bytes; gatt.writeCharacteristic(char)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            bluetoothGATT?.writeCharacteristic(char, dataBytes, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
        } else {
            @Suppress("DEPRECATION")
            char.value = dataBytes
            @Suppress("DEPRECATION")
            bluetoothGATT?.writeCharacteristic(char)
        }
    }

    private fun enableNotifications(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return
        gatt.setCharacteristicNotification(characteristic, true)
        val descriptor = characteristic.getDescriptor(UUID.fromString(CCCD_UUID))
        descriptor?.let {
            // Determine value based on properties (simplified)
            val value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                gatt.writeDescriptor(it, value)
            } else {
                @Suppress("DEPRECATION")
                it.value = value
                @Suppress("DEPRECATION")
                gatt.writeDescriptor(it)
            }
        }
    }

    // --- Helpers ---

    private fun updateDeviceConnectionState(address: String, isConnected: Boolean) {
        _discoveredDevices.update { list ->
            list.map {
                if (it.device.address == address) it.copy(isConnected = isConnected, isSelected = isConnected) else it
            }
        }
    }

    private fun scheduleGattRetry(device: BluetoothDevice) {
        if (connectionAttempts >= maxConnectionAttempts) {
            Log.e("GATT","Reached maximum retry attempts for ${device.address}.")
            clearConnectionAttempts()
            return
        }
        connectionAttempts++
        val delay = (baseConnectionAttemptDelayMs * connectionAttempts).coerceAtMost(maxConnectionAttemptDelayMs)
        Log.i("GATT","Retry attempt $connectionAttempts for ${device.address} in ${delay}ms.")

        connectionAttemptHandler.postDelayed({
            establishConnection(device)
        }, delay)
    }

    private fun clearConnectionAttempts() {
        connectionAttemptHandler.removeCallbacksAndMessages(null)
        connectionAttempts = 0
        connectionAttemptDevice = null
    }

    private fun hasPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}