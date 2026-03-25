package com.averyvi.bilbo.ui.fragments

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.averyvi.bilbo.R

@Composable
fun NewInstrumentTextInput(
    label: @Composable () -> Unit,
    placeholder: @Composable () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon:  @Composable () -> Unit = { Icon( painter = painterResource(R.drawable.radio_button_checked_24px), contentDescription = null ) },
    modifier: Modifier = Modifier,
    brushColorList: List<Color> = listOf( MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary ),
    gradientTextStyle: TextStyle = TextStyle(fontSize = TextUnit.Unspecified),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
){
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(
        text = value,
        style = gradientTextStyle
    )
    val gradientEndX = if (textLayoutResult.size.width > 0) {
        textLayoutResult.size.width.toFloat()
    } else {
        1f
    }

    TextField(
        label = label,
        placeholder = placeholder,
        value = value,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(24.dp),
        textStyle = TextStyle(brush = Brush.linearGradient(
            colors = brushColorList,
            end = Offset(gradientEndX, 0f)
        )),
        keyboardOptions = keyboardOptions,
        singleLine = true,
    )
}