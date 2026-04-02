package com.averyvi.bilbo.ui.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.averyvi.bilbo.R
import com.averyvi.bilbo.ui.fragments.IsHarmonicText
import com.averyvi.bilbo.ui.fragments.NoteOctiveDisplay
import com.averyvi.bilbo.data.uiState.TuningViewModel

@Composable
fun BILBONavPill(
    tuningViewModel: TuningViewModel,
    onRouteButtonClicked: (Boolean) -> Unit,
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
                NoteOctiveDisplay(tuningViewModel.note.collectAsState().value.name, tuningViewModel.octive.collectAsState().value.toString())

                VerticalDivider(
                    modifier = Modifier.height(5.dp),
                    thickness = 5.dp
                )

                IsHarmonicText(tuningViewModel.pitch.collectAsState().value)

                VerticalDivider(
                    modifier = Modifier.height(5.dp),
                    thickness = 5.dp
                )

                Button(
                    onClick = { onRouteButtonClicked(false) },
                    contentPadding = PaddingValues(5.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonColors(
                        MaterialTheme.colorScheme.surfaceContainer,
                        MaterialTheme.colorScheme.onSurface,
                        MaterialTheme.colorScheme.surfaceContainerHigh,
                        MaterialTheme.colorScheme.onSurface
                    )
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