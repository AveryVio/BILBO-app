package com.averyvi.bilbo.ui.fragments

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.averyvi.bilbo.R
import com.averyvi.bilbo.definitions.MusicalNote
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.sin

class InstrumentViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _freq = MutableStateFlow("")
    val freq: StateFlow<String> = _freq.asStateFlow()

    private val _note = MutableStateFlow(MusicalNote.A)
    val note: StateFlow<MusicalNote> = _note.asStateFlow()

    private val _octive = MutableStateFlow(4)
    val octive: StateFlow<Int> = _octive.asStateFlow()

    fun updateName(newName: String) { _name.value = newName }
    fun updateFreq(newFreq: String) { _freq.value = newFreq }
    fun updateNote(newNote: MusicalNote) { _note.value = newNote }
    fun updateOctive(newOctive: Int) { _octive.value = newOctive }

    fun resetValues() {
        _name.value = ""
        _freq.value = ""
        _note.value = MusicalNote.A
        _octive.value = 0
    }

    fun addInstrument() {
        val currentName = _name.value
        val currentFreq = _freq.value
    }
}

@Composable
fun InstrumentShelfItem(
    @DrawableRes InstrumentImageResource: Int,
    name: String,
    comment: String = "",
    inModifier: Modifier = Modifier
){
    Card(
        inModifier
            .clip(RoundedCornerShape(18.dp)),
        colors = CardColors(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface,
            MaterialTheme.colorScheme.surfaceContainerHigh,
            MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column {
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
                fontWeight = FontWeight(800),
                fontSize = 18.sp,

                modifier = Modifier.fillMaxWidth().padding(vertical = 0.dp, horizontal =  4.dp)
            )
            Text(
                text = comment,
                fontSize = 14.sp,
                lineHeight = 12.sp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 0.dp, horizontal = 4.dp)
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
    Box(){
        Card(
            onClick = onCardClick,
        ) {
            Text(
                text = activatorName,
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