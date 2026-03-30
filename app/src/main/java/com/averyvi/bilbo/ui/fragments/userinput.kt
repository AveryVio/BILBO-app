package com.averyvi.bilbo.ui.fragments

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.averyvi.bilbo.definitions.MusicalNote

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

@Composable
fun NewInstrumentSelector(
    selectedName: String,
    onNameChange: (String) -> Unit,
    selectedFreqString: String,
    onFreqChange: (String) -> Unit,
    selectedNote: MusicalNote,
    onSelectedNoteChange: (MusicalNote) -> Unit,
    selectedOctive: Int,
    onSelectedOctiveChange: (Int) -> Unit
    ){
    var noteIsExpanded by remember { mutableStateOf(false) }
    var octiveIsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        NewInstrumentTextInput(
            label = @Composable { Text(stringResource(R.string.NewInstrumentName)) },
            placeholder = @Composable { Text("Foobar") },
            value = selectedName,
            onValueChange = { onNameChange(it) },
            leadingIcon = @Composable {
                Icon(
                    painter = painterResource(R.drawable.radio_button_checked_24px), // todo update
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            brushColorList = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.tertiary
            )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NewInstrumentTextInput(
                label = @Composable { Text(stringResource(R.string.FreqName)) },
                placeholder = @Composable { Text(stringResource(R.string.FreqName)) },
                value = selectedFreqString,
                onValueChange = { onFreqChange(it) },
                leadingIcon = @Composable {
                    Icon(
                        painter = painterResource(R.drawable.freq),
                        contentDescription = null
                    )
                },
                modifier = Modifier,
                brushColorList = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.tertiary
                ),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            NewInstrumentDropdown(
                activatorName = selectedNote.name,
                expanded = noteIsExpanded,
                onCardClick = { noteIsExpanded = !noteIsExpanded },
                onDismissRequest = { noteIsExpanded = false },
                content = @Composable {
                    MusicalNote.entries.forEach { note ->
                        DropdownMenuItem(
                            onClick = {
                                onSelectedNoteChange(note)
                                noteIsExpanded = false
                            },
                            text = { Text(note.name) },
                        )
                    }
                },
            )

            NewInstrumentDropdown(
                activatorName = selectedOctive.toString(),
                expanded = octiveIsExpanded,
                onCardClick = { octiveIsExpanded = !octiveIsExpanded },
                onDismissRequest = { octiveIsExpanded = false },
                content = @Composable {
                    (-2..8).forEach { octive ->
                        DropdownMenuItem(
                            onClick = {
                                onSelectedOctiveChange(octive)
                                octiveIsExpanded = false
                            },
                            text = { Text(octive.toString()) },
                        )
                    }
                },
            )
        }
    }
}