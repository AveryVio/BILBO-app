package com.averyvi.bilbo.ui.app

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.notui.SelectableBluetoothDevice
import com.averyvi.bilbo.ui.theme.BilboTheme

@Composable
fun DeviceList(
    devices: List<SelectableBluetoothDevice>,
    onDeviceSelected: (SelectableBluetoothDevice) -> Unit
){
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardColors(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.onSurface
            ),
            onClick = { isExpanded = !isExpanded }
        ) {
            Row(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.BTConnectedDevices),
                    modifier = Modifier.padding(start = 10.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
                )
                Icon(
                    painter = painterResource( if(!isExpanded) R.drawable.bluetooth_searching else R.drawable.bluetooth ),
                    contentDescription = null,
                    modifier = if(!isExpanded) Modifier.size(40.dp) else Modifier.size(40.dp).rotate(90f) ,
                )
            }
        }

        AnimatedVisibility(isExpanded) {
            LazyColumn( modifier = Modifier.fillMaxWidth()) {
                //todo finish

            }
        }
    }
}