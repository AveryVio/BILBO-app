package com.averyvi.bilbo.ui.fragments

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R

@Composable
fun PitchDiffView(pitchDiff: Int){
    Box(){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val currentDiff = pitchDiff

            val sliderRange = if (currentDiff < 0) {
                currentDiff.toFloat()..0f
            } else {
                0f..currentDiff.toFloat()
            }

            Spacer(Modifier.requiredHeight(30.dp))
            RangeSlider(
                modifier = Modifier.width(300.dp),
                value = sliderRange,
                valueRange = -100f..100f,
                onValueChange = { Log.d("jfklds", pitchDiff.toString()) },
                colors = SliderColors(
                    disabledThumbColor = MaterialTheme.colorScheme.primary,
                    disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
                    disabledActiveTickColor = MaterialTheme.colorScheme.primary,
                    disabledInactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    disabledInactiveTickColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    activeTickColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTickColor = MaterialTheme.colorScheme.primary,
                ),
                enabled = false
            )
        }
    }
}

@Composable
fun IsHarmonicText(pitchDiff: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("${pitchDiff}")

        Text(stringResource(R.string.Cents))
    }
}

@Composable
fun NoteOctiveDisplay(note:String, octive:String){
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = note,
            fontSize = 40.sp,
        )
        Text(
            text = octive,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun FilterRangeDisplay(FilterRange:Int){
    Row(
        horizontalArrangement = Arrangement.spacedBy((-2).dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            painter = painterResource( R.drawable.freq ),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 4.dp, end = 3.dp)
        )
        Text(
            text = FilterRange.toString(),
            fontSize = 20.sp
        )
    }
}