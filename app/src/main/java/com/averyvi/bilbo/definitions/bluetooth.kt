package com.averyvi.bilbo.definitions

import android.bluetooth.BluetoothDevice

data class SelectableBluetoothDevice(
    val device: BluetoothDevice,
    var isSelected: Boolean = false,
    var isConnected: Boolean = false
)