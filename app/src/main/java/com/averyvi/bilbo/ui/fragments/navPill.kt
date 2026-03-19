package com.averyvi.bilbo.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.Routes

@Composable
fun BILBONavPill(
    note: State<String>,
    octive: State<String>,
    pitch: MutableState<Int> = mutableIntStateOf(0),
    onRouteButtonClicked: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NoteOctiveDisplay(note.value, octive.value)

                VerticalDivider(
                    modifier = Modifier.height(5.dp),
                    thickness = 5.dp
                )

                IsHarmonicText(pitch)

                VerticalDivider(
                    modifier = Modifier.height(5.dp),
                    thickness = 5.dp
                )

                Card(
                    onClick = onRouteButtonClicked
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy((-2).dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Icon(
                            painter = painterResource( R.drawable.freq ),
                            contentDescription = null,
                            modifier = Modifier.padding(bottom = 4.dp, end = 3.dp)
                        )
                    }
                }
            }
        }
    }
}