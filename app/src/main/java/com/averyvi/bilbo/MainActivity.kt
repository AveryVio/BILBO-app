package com.averyvi.bilbo

import android.Manifest
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
import com.averyvi.bilbo.ui.theme.BilboTheme
import kotlin.contracts.contract

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BilboTheme {
                // check bluetooth avalibility
                val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
                val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

                // check if the device has bt and if it is enabled
                val permissionsNeeded = arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )

                if (bluetoothAdapter?.isEnabled == false) {
                    permissionsNeeded.forEach { permission ->
                         if(ActivityCompat.checkSelfPermission(
                                this,
                                permission
                            ) != PackageManager.PERMISSION_GRANTED
                         ) {
                             ActivityCompat.requestPermissions(
                                 this,
                                 arrayOf(permission),
                                 1
                             )
                         }
                    }
                }
                AppUI()
            }
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