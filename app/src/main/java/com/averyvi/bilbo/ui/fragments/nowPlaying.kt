package com.averyvi.bilbo.ui.fragments

import android.util.Log
import android.widget.ProgressBar
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R
import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.withSign


@Composable
fun PitchDiffView(pitchDiff: MutableState<Int>){
    Box(){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val currentDiff = pitchDiff.value

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
fun IsHarmonicText(pitchDiff: MutableState<Int>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("${pitchDiff.value}")

        Text(stringResource(R.string.Cents))
    }
}

@Composable
fun NoteOctiveDisplay(Note:String, Octive:String){
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = Note,
            fontSize = 40.sp,
        )
        Text(
            text = Octive,
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