package com.averyvi.bilbo.ui.fragments

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun InstrumentShelfItem(
    @DrawableRes InstrumentImageResource: Int,
    name: String,
    comment: String = "",
    columnWidth: Dp,
    inModifier: Modifier = Modifier
){
    val boxHeightMultiplier = (if (comment != "") (if (comment.length > 9) 1.6 else 1.45) else 1.25).toFloat()
    val boldFontSizeMultiplier = if(columnWidth < 100.dp) 8 else 9
    val normalFontSizeMultiplier = if(columnWidth < 100.dp) 6 else 7

    val boxModifier = inModifier
        .padding(8.dp)
        .clip(RoundedCornerShape(11.dp))
        .width(columnWidth)
        .background(Color( 100, 100, 100 ))


    Card(boxModifier) {
        Column {
            Image(
                painter = painterResource( InstrumentImageResource ),
                contentDescription = null,
                modifier = inModifier.width(columnWidth).height(columnWidth).clip(RoundedCornerShape(11.dp))
            )
            Text(
                text = name,
                fontWeight = FontWeight(800),
                fontSize = (2 * boldFontSizeMultiplier).sp,

                modifier = inModifier.fillMaxWidth().padding(vertical = 0.dp, horizontal =  4.dp)
            )
            Text(
                text = comment,
                fontSize = (2 * normalFontSizeMultiplier).sp,
                lineHeight = 12.sp,
                modifier = inModifier.fillMaxWidth().padding(vertical = 0.dp, horizontal = 4.dp)
            )
        }
    }
}