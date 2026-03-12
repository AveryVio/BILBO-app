package com.averyvi.bilbo.bluetooth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.averyvi.bilbo.definitions.FirstHarmonic
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

class BilboViewModel(application: Application) : AndroidViewModel(application) {

    // Initialize Manager with Application Context
    private val bluetoothManager = BilboBluetoothManager(application.applicationContext)

    // Expose flow to UI
    val discoveredDevices: StateFlow<List<SelectableBluetoothDevice>> = bluetoothManager.discoveredDevices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun startScan() = bluetoothManager.startScan()
    fun stopScan() = bluetoothManager.stopScan()

    fun toggleConnection(deviceWrapper: SelectableBluetoothDevice) {
        if (deviceWrapper.isConnected) {
            bluetoothManager.disconnectGATT(deviceWrapper.device.address)
        } else {
            bluetoothManager.connectToDevice(deviceWrapper.device)
        }
    }

    fun sendFrequency(firstHarmonic: FirstHarmonic) {
        val payload = String.format(Locale.US, "%.2f\r\n", firstHarmonic.frequency)
        bluetoothManager.sendData(payload)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothManager.stopScan()
    }
}