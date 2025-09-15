package com.averyvi.bilbo.simpleuis

import android.widget.Button
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.averyvi.bilbo.R
import java.sql.Ref

@Composable
fun karta() {
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