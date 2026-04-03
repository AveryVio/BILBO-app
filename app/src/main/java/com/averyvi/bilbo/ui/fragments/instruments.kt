package com.averyvi.bilbo.ui.fragments

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.ui.theme.boldText
import com.averyvi.bilbo.ui.theme.normalText

@Composable
fun InstrumentShelfItem(
    @DrawableRes InstrumentImageResource: Int,
    name: String,
    inModifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Card(
        modifier = inModifier
            .clip(RoundedCornerShape(18.dp)),
        colors = CardColors(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface
        ),
        onClick = { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Image(
                painter = painterResource( InstrumentImageResource ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(
                text = name,
                style = boldText(),
                fontSize = 18.sp,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth().padding(bottom =  8.dp)
            )
        }
    }
}

@Composable
fun NewInstrumentDropdown(
    activatorName:String,
    expanded: Boolean,
    onCardClick: () -> Unit,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
){
    Box(
        modifier = Modifier.width(IntrinsicSize.Max)
    ){
        Card(
            onClick = onCardClick,
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = activatorName,
                style = normalText(),
                fontSize = 47.sp,
                modifier = Modifier.padding(3.dp)
            )
        }
        if (expanded) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = onDismissRequest,
                shape = RoundedCornerShape(24.dp),
            ) { content() }
        }
    }
}