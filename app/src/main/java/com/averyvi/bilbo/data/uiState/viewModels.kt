package com.averyvi.bilbo.data.uiState

import androidx.lifecycle.ViewModel
import com.averyvi.bilbo.R
import com.averyvi.bilbo.data.storage.InstrumentDBRow
import com.averyvi.bilbo.definitions.MusicalNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TuningViewModel : ViewModel() {
    private val _note = MutableStateFlow(MusicalNote.A)
    val note: StateFlow<MusicalNote> = _note.asStateFlow()

    private val _octive = MutableStateFlow(4)
    val octive: StateFlow<Int> = _octive.asStateFlow()

    private val _pitch = MutableStateFlow(0)
    val pitch: StateFlow<Int> = _pitch.asStateFlow()



    fun updateNote(newNote: MusicalNote) { _note.value = newNote }
    fun updateOctive(newOctive: Int) { _octive.value = newOctive }
    fun updatePitch(newPitch: Int) { _pitch.value = newPitch}

    fun incrementNote() { _note.value = MusicalNote.entries[_note.value.ordinal + 1] }
    fun incrementOctive() { _octive.value++ }
    fun incrementPitch() { _pitch.value++}

    fun resetNote() { _note.value = MusicalNote.entries[0] }
    fun resetOctive() { _octive.value = -2 }
    fun resetPitch() { _pitch.value = 0 }

    fun resetValues() {
        _note.value = MusicalNote.A
        _octive.value = 4
        _pitch.value = 0
    }
}

class CurrentInstrumentViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _icon = MutableStateFlow(R.drawable.radio_button_checked_24px)
    val icon: StateFlow<Int> = _icon.asStateFlow()

    private val _freq = MutableStateFlow("")
    val freq: StateFlow<String> = _freq.asStateFlow()

    private val _note = MutableStateFlow(MusicalNote.A)
    val note: StateFlow<MusicalNote> = _note.asStateFlow()

    private val _octive = MutableStateFlow(4)
    val octive: StateFlow<Int> = _octive.asStateFlow()

    fun updateName(newName: String) { _name.value = newName }
    fun updateIcon(newIcon: Int) { _icon.value = newIcon }
    fun updateFreq(newFreq: String) { _freq.value = newFreq }
    fun updateNote(newNote: MusicalNote) { _note.value = newNote }
    fun updateOctive(newOctive: Int) { _octive.value = newOctive }

    fun resetValues() {
        _name.value = ""
        _icon.value = R.drawable.radio_button_checked_24px
        _freq.value = ""
        _note.value = MusicalNote.A
        _octive.value = 0
    }
}

class NewInstrumentViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _icon = MutableStateFlow(R.drawable.radio_button_checked_24px)
    val icon: StateFlow<Int> = _icon.asStateFlow()

    private val _freq = MutableStateFlow("")
    val freq: StateFlow<String> = _freq.asStateFlow()

    private val _note = MutableStateFlow(MusicalNote.A)
    val note: StateFlow<MusicalNote> = _note.asStateFlow()

    private val _octive = MutableStateFlow(4)
    val octive: StateFlow<Int> = _octive.asStateFlow()

    fun updateName(newName: String) { _name.value = newName }
    fun updateIcon(newIcon: Int) { _icon.value = newIcon }
    fun updateFreq(newFreq: String) { _freq.value = newFreq }
    fun updateNote(newNote: MusicalNote) { _note.value = newNote }
    fun updateOctive(newOctive: Int) { _octive.value = newOctive }

    fun resetValues() {
        _name.value = ""
        _icon.value = R.drawable.radio_button_checked_24px
        _freq.value = ""
        _note.value = MusicalNote.A
        _octive.value = 0
    }
}