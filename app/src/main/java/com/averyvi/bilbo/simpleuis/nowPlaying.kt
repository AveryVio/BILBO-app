package com.averyvi.bilbo.simpleuis

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
        shape = RoundedCornerShape(15.dp),
        /*colors = CardColors(
            Color.Gray,
            Color.Blue,
            Color.Black,
            Color.White,
        )*/
    ) {
        Row {
            Button(
                onClick = {},
                colors = ButtonColors(
                    Color.Gray,
                    Color.Blue,
                    Color.Black,
                    Color.White,
                ),
                enabled = false
            ) { }
            Text("kfjsdofis  ")
            Text("kfjsdofis  ")
            Text("kfjsdofis  ")
        }
    }
}