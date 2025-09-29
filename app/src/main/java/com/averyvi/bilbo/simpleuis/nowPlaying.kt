package com.averyvi.bilbo.simpleuis

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.averyvi.bilbo.R
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.round
import kotlin.math.withSign

@Composable
fun NoteDisplayCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardColors(
            Color.DarkGray,
            Color.Blue,
            Color.Black,
            Color.White,
        )
    ) {
        val CardButtonColors = ButtonColors(
            Color.Gray,
            Color.Black,
            Color.DarkGray,
            Color.White
        )
        Row(
            modifier = Modifier.padding(horizontal = 3.dp)
        ) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(16.dp),
                colors = CardButtonColors,
            ) { Text("C") }
            Button(
                onClick = {},
                shape = RoundedCornerShape(16.dp),
                colors = CardButtonColors,
                enabled = false
            ) { Text("D") }
            Button(
                onClick = {},
                shape = RoundedCornerShape(16.dp),
                colors = CardButtonColors,
            ) { Text("E") }
        }
    }
}
@Composable
fun PitchDiffView(
    pitchDiff: MutableState<Double>,
    width: Int,
    height: Int,
){
    val halfHeight = height.toDouble() / 2

    Box {
        Box(modifier = Modifier
            .width(Dp(width.toFloat()))
            .height(Dp(height.toFloat()))
            .background(color = Color(255, 255, 255, 255))
        ){}

        Image(
            painter = painterResource( R.drawable.decreasing_sine ),
            contentDescription = null,
            modifier = Modifier
                .offset(x = Dp((width / 2.5).toFloat()))
                .height(height.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(width.dp)
                .offset(0.dp, Dp((  halfHeight - (pitchDiff.value * halfHeight) - (height.toFloat()/36) ).toFloat()) )
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(110.dp))
                    .width(Dp(width.toFloat()/7))
                    .height(Dp(height.toFloat()/18))
                    .background(color = Color(200, 50, 100))
            ) {}
        }
    }
}

@Composable
fun IsHarmonicText(pitchDiff: MutableState<Double>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
    ) {
        var diff = pitchDiff.value.withSign(1.0)
        diff = round(diff * 100) / 100

        if(ceil(pitchDiff.value) > 0.0){
            Text(stringResource(R.string.YouAreXUnderAbove1) + diff.toString() + stringResource(R.string.YouAreXUnderAbove2Above))
        }
        else {
            Text(stringResource(R.string.YouAreXUnderAbove1) + diff.toString() + stringResource(R.string.YouAreXUnderAbove2Under))
        }
    }
}