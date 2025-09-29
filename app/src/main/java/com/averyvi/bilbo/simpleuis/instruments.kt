package com.averyvi.bilbo.simpleuis

import android.content.res.Resources
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R
import kotlinx.coroutines.delay
import java.nio.file.WatchEvent
import kotlin.math.sin

@Composable
fun InstrumentShelfItem(
    @DrawableRes InstrumentImageResource: Int,
    name: String,
    comment: String = "",
    columnWidth: Dp,
    inModifier: Modifier = Modifier
){
    val boxModifier = inModifier
        .padding(8.dp)
        .clip(RoundedCornerShape(11.dp))
        .width(columnWidth)
        .height(Dp((columnWidth.value * if (comment != "") 1.25 else 0.95).toFloat()))
        .background(Color( 100, 100, 100 ))


    Box(boxModifier) {
        Column {
            Image(
                painter = painterResource( InstrumentImageResource ),
                contentDescription = null,
                modifier = inModifier.fillMaxWidth().clip(RoundedCornerShape(11.dp))
            )
            Text(
                text = name,
                fontWeight = FontWeight(800),
                fontSize = 17.sp,
                modifier = inModifier.fillMaxWidth().padding(vertical = 0.dp, horizontal =  4.dp)
            )
            Text(
                text = comment,
                fontSize = 14.sp,
                lineHeight = 12.sp,
                modifier = inModifier.fillMaxWidth().padding(vertical = 0.dp, horizontal = 4.dp)
            )
        }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun allInstruments(){
    val pitch = remember {
        mutableDoubleStateOf(0.0)
    }
    val pitchStep = remember {
        mutableDoubleStateOf(0.0)
    }

    LaunchedEffect (Unit) {
        while(true) {
            pitchStep.doubleValue += 0.1
            pitch.doubleValue = sin(pitchStep.doubleValue)
            delay(50)
        }
    }

    Column {
        InstrumentShelfItem(R.drawable.ic_launcher_background, "vera", "former teammate", 100.dp)
        PitchDiffView(pitch, 260, 400)
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Preview(showBackground = true)
@Composable
fun InstrumentShelfItemPreview(){
    InstrumentShelfItem(R.drawable.ic_launcher_background, "vera", "former teammate", 100.dp)
}

@Preview(showBackground = true)
@Composable
fun PitchDiffViewPreview(){
    val pitch = remember {
        mutableDoubleStateOf(0.3)
    }

    PitchDiffView(pitch, 130, 400)
}