package com.averyvi.bilbo

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.averyvi.bilbo.data.bluetooth.BilboBluetoothManager
import com.averyvi.bilbo.data.bluetooth.BilboViewModel
import com.averyvi.bilbo.data.frop.buildProfileChangeMessage
import com.averyvi.bilbo.data.storage.AppDatabase
import com.averyvi.bilbo.data.uiState.TuningViewModel
import com.averyvi.bilbo.ui.theme.BilboTheme


class MainActivity : ComponentActivity() {

    // Instantiate ViewModel
    private val bilboViewModel: BilboViewModel by viewModels()
    private var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val btManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = btManager.adapter

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "instruments"
        ).build()
        val userDao = db.userDao()

        beginBluetooth()

        setContent {
            val factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TuningViewModel(bilboViewModel.bluetoothManager) as T
                }
            }

            val tuningViewModel: TuningViewModel = viewModel(factory = factory)

            BilboTheme {
                // Observe data from ViewModel
                val discoveredDevices by bilboViewModel.discoveredDevices.collectAsState()

                AppUI(
                    tuningViewModel = tuningViewModel,
                    userDao = userDao,
                    deviceList = discoveredDevices,
                    updateInstrument = { freq: Int, note: Int, octive: Int ->
                        val profileMessage = buildProfileChangeMessage(frequency = freq, positionInOctive = note, octive = octive)
                        bilboViewModel.bluetoothManager.sendData(profileMessage)
                    },
                    onDeviceSelected = { selectedDevice ->
                        // Delegate logic to ViewModel
                        bilboViewModel.toggleConnection(selectedDevice)
                    },
                )
            }
        }
    }

    // --- Permissions & Lifecycle Logic ---

    private fun beginBluetooth() {
        Log.d("Permissions", "Checking for necessary permissions.")
        if (checkEnablePermissions()) {
            if (bluetoothAdapter?.isEnabled == false) {
                Log.d("BluetoothBegin", "Bluetooth disabled, asking user to enable it.")
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enablePermission.launch(enableIntent)
            } else {
                Log.d("BluetoothBegin", "Scanning begins.")
                bilboViewModel.startScan()
            }
        }
        else {
            Log.d("Permissions", "App does not have the permissions for bluetooth")
        }
    }

    private fun checkEnablePermissions(): Boolean {
        val permissionsNeeded = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        val permissionsNotGranted = permissionsNeeded.filterNot {
            ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        return if (permissionsNotGranted.isEmpty()) {
            Log.d("Permissions", "All necessary permissions already granted.")
            true
        } else {
            Log.d("Permissions", "Requesting necessary permissions.")
            Log.d("Permissions", "Requesting:")
            Log.d("Permissions", permissionsNotGranted.joinToString("; "))
            requestPermissions.launch(permissionsNeeded)
            false
        }
    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                Log.d("Permissions", "All permissions granted.")
                if (bluetoothAdapter?.isEnabled == true) bilboViewModel.startScan()
            } else {
                Log.w("Permissions", "Permissions denied.")
            }
        }

    private val enablePermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                bilboViewModel.startScan()
            }
        }
}