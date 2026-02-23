package com.averyvi.bilbo.ui.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
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
fun NoteDisplayCard(Note: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Text(
            text = Note,
            fontSize = 40.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}
@Composable
fun PitchDiffView(
    pitchDiff: MutableState<Double>,
    width: Int,
    height: Int,
){
    val halfHeight = height.toDouble() / 2

    Box {/*
        Box(modifier = Modifier
            .width(Dp(width.toFloat()))
            .height(Dp(height.toFloat()))
            .background(color = Color(255, 255, 255, 255))
        ){}*/

        Image(
            painter = painterResource( R.drawable.tunebar ),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Red),
            modifier = Modifier
                .offset(x = Dp((width / 100).toFloat()))
                .height(height.dp)
                .width(width.dp)
        )


        Image(
            painter = painterResource( R.drawable.tuneline ),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.Red),
            modifier = Modifier
                .offset(x = Dp((width / 2.6).toFloat()))
                .height(height.dp)
                .graphicsLayer(
                    transformOrigin = TransformOrigin(
                        pivotFractionX = 0.5f,
                        pivotFractionY = 0.8f,
                    ),
                    rotationZ = ((pitchDiff.value * 35).toFloat()),

                ),
        )
    }
}

@Composable
fun IsHarmonicText(pitchDiff: MutableState<Double>) {
    Card(
        colors = CardColors(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var diff = pitchDiff.value.withSign(1.0)
            diff = round(diff * 100)

            if (ceil(pitchDiff.value) > 0) Text("+" + diff.roundToInt().toString())
            else Text("-" + diff.roundToInt().toString())

            Text(stringResource(R.string.Cents))
        }
    }
}

@Composable
fun NoteOctiveDisplay(octive:Int){
    Card(
        colors = CardColors(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Text(
            text = stringResource(R.string.OctiveName) + ": " + octive.toString(),
            modifier = Modifier.padding(10.dp).width(130.dp),
            textAlign = TextAlign.Center
        )
    }
}