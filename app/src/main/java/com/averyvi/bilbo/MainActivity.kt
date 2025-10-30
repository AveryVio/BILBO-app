package com.averyvi.bilbo

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.averyvi.bilbo.ui.theme.BilboTheme
import kotlin.contracts.contract

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothManager: BluetoothManager
    private var bluetoothAdapter: BluetoothAdapter? = null

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
    val enablePermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("Permissions", "Bluetooth enabled by user.")
                enablePermissionReturn = true

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
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enablePermission.launch(enableIntent)
            if(enablePermissionReturn?:false){
                Log.d("BluetoothBegin", "Scanning begins.")
                //startBluetoothScan()
            }
        } else if (bluetoothAdapter != null) {
            Log.d("BluetoothBegin", "Scanning begins.")
            //startBluetoothScan()
        }
    }





































    fun findDevice(){
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        // check if the device has bt and if it is enabled and connected
        if (bluetoothAdapter?.isEnabled == false) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    1
                )

                /*registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    val allPermissionsGranted = permissions.all { it.value }
                    if (allPermissionsGranted) {
                        Log.d("MyActivity", "All permissions granted.")
                        proceedWithBluetooth()
                    } else {
                        Log.w("MyActivity", "User denied one or more permissions. Cannot perform BLE operations.")
                    }
                }*/

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
        }

        // check paired devices for our device
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
        }


    }
}