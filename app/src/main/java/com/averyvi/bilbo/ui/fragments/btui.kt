package com.averyvi.bilbo.ui.fragments

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.definitions.SelectableBluetoothDevice
import com.averyvi.bilbo.ui.theme.boldText
import com.averyvi.bilbo.ui.theme.semiBoldText

@Composable
@SuppressLint("MissingPermission")
fun DeviceList(
    devices: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column() {
        Card(
            modifier = Modifier
                .padding(16.dp),
            onClick = { isExpanded = !isExpanded },
            colors = CardColors(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(24.dp),
        ) {
            Card(
                modifier = Modifier
                    .padding(4.dp),
                colors = CardColors(
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSurface,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(22.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.BTConnectedDevices),
                        style = boldText(),
                        fontSize = 18.sp
                    )
                    Icon(
                        painter = painterResource(if (!isExpanded) R.drawable.bluetooth_searching else R.drawable.bluetooth),
                        contentDescription = null,
                        modifier = if (!isExpanded) Modifier.size(40.dp) else Modifier
                            .size(40.dp)
                            .rotate(90f),
                    )
                }
            }
            if (isExpanded) {
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    shape = RoundedCornerShape(24.dp),
                ) {
                    devices.forEachIndexed { index, device ->
                        DropdownMenuItem(
                            onClick = {
                                onDeviceSelected(device)
                                isExpanded = false
                                      },
                            text = { Text(
                                text = device.device.name + "\n" + device.device.address,
                                style = semiBoldText(),
                                fontSize = 16.sp
                            ) },
                            leadingIcon = { Text(
                                text = index.toString(),
                                style = boldText(),
                                fontSize = 20.sp
                            ) },
                            colors = MenuItemColors(
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.colorScheme.inverseOnSurface,
                                MaterialTheme.colorScheme.inverseOnSurface,
                                MaterialTheme.colorScheme.inverseOnSurface,
                            ),
                        )
                    }
                }
            }
        }
    }
}