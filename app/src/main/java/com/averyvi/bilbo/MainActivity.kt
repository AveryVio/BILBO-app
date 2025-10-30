package com.averyvi.bilbo

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.averyvi.bilbo.ui.theme.BilboTheme
import kotlin.contracts.contract

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothManager: BluetoothManager
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val bleScanner by lazy { bluetoothAdapter?.bluetoothLeScanner }

    var isScanning = false
    val discoveredDevices = mutableStateListOf<SelectableBluetoothDevice>()
    val scanTimeoutHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        enableEdgeToEdge()
        beginBluetooth()
        setContent {
            BilboTheme {
                AppUI()
            }
        }
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

        val permissionsNotGranted = permissionsNeeded.filterNot { hasPermission(it) }

        if(permissionsNotGranted.isEmpty()){
            Log.d("Permissions", "All necessary permissions already granted.")
            return true
        } else{
            Log.d("Permissions", "Requesting necessary permissions.")
            requestPermissions.launch(permissionsNeeded)
            return requestPermissionsReturn?:false
        }
    }

    var requestPermissionsReturn: Boolean? = null
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
                requestPermissionsReturn = true
            } else {
                Log.w("Permissions", "User denied one or more permissions. Cannot perform BLE operations.")
                requestPermissionsReturn = true
            }
        }

    var enablePermissionReturn: Boolean? = null
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
                startSBTScann()

            } else {
                Log.d("Permissions", "User denied Bluetooth enabling.")
                enablePermissionReturn = false
            }
        }

    fun hasPermission(permission: String): Boolean {
        val granted = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            Log.w("Permissions", "Permission not granted: $permission")
        }
        return granted
    }

    /*begin bluetooth**********************************************************************************************/

    fun beginBluetooth() {
        Log.d("Permissions", "Checking for necessary permissions.")
        if(checkEnablePermissions()){
            proceedWithBluetooth()
        } else{
            Log.d("Permissions", "App does not have the permissions for bluetooth")
        }
    }

    fun proceedWithBluetooth(){
        if (bluetoothAdapter?.isEnabled == false) {
            Log.d("BluetoothBegin", "Bluetooth disabled, asking user to enable it.")
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enablePermission.launch(enableIntent)
            if(enablePermissionReturn?:false){
            }
        } else if (bluetoothAdapter != null) {
            Log.d("BluetoothBegin", "Scanning begins.")
            startSBTScann()
        }
    }

    /*bluetooth scanning**********************************************************************************************/

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startSBTScann(){
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) return
        if (isScanning) return
        discoveredDevices.clear()
        isScanning = true

        scanTimeoutHandler.postDelayed({ stopBTScan() }, 10000)
        bleScanner?.startScan(scanCallback)
        Log.d("BluetoothScan", "Bluetooth scanning started.")
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopBTScan() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) return
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

    // kouknout do referenci na 582 pro discoveredDevices
}

/******************************************************************************************************************************************/
/******************************************************************************************************************************************/

data class SelectableBluetoothDevice(
    val device: BluetoothDevice,
    var isSelected: Boolean = false,
    var isConnected: Boolean = false
)