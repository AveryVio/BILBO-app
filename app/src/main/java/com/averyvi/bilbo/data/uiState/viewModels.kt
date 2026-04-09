package com.averyvi.bilbo.data.uiState

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.averyvi.bilbo.R
import com.averyvi.bilbo.data.bluetooth.BilboBluetoothManager
import com.averyvi.bilbo.data.frop.TuningData
import com.averyvi.bilbo.definitions.MusicalNote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TuningViewModel(private val bluetoothManager: BilboBluetoothManager) : ViewModel() {
    private val _note = MutableStateFlow(MusicalNote.A)
    val note: StateFlow<MusicalNote> = _note.asStateFlow()

    private val _octive = MutableStateFlow(4)
    val octive: StateFlow<Int> = _octive.asStateFlow()

    private val _pitch = MutableStateFlow(0)
    val pitch: StateFlow<Int> = _pitch.asStateFlow()

    private val _freq = MutableStateFlow(0)
    val freq: StateFlow<Int> = _freq.asStateFlow()

    init {
        viewModelScope.launch {
            bluetoothManager.incomingTuningData.collect { TuningData: TuningData ->
                updateUI(TuningData)
            }
        }
    }

    fun updateNote(newNote: MusicalNote) { _note.value = newNote }
    fun updateOctive(newOctive: Int) { _octive.value = newOctive }
    fun updatePitch(newPitch: Int) { _pitch.value = newPitch}
    fun updateFreq(newFreq: Int) { _freq.value = newFreq}

    fun incrementNote() { _note.value = MusicalNote.entries[_note.value.ordinal + 1] }
    fun incrementOctive() { _octive.value++ }
    fun incrementPitch() { _pitch.value++}
    fun incrementFreq() { _freq.value++}

    fun resetNote() { _note.value = MusicalNote.entries[0] }
    fun resetOctive() { _octive.value = -2 }
    fun resetPitch() { _pitch.value = 0 }
    fun resetFreq() { _freq.value = 0 }

    fun resetValues() {
        _note.value = MusicalNote.A
        _octive.value = 4
        _pitch.value = 0
        _freq.value = 0
    }

    private fun updateUI(data: TuningData) {
        _note.value = MusicalNote.entries[data.positionInOctave]
        _octive.value = data.octive
        _pitch.value = data.difference
        _freq.value = data.frequency
        // Update your UI state variables here
        // e.g., currentFrequency.value = data.frequency
    }
}

class InstrumentProfileViewModel : ViewModel() {

    private val _currentInstrument = MutableStateFlow(
        UsableInstrumentProfile(
            name = "",
            icon = R.drawable.radio_button_checked_24px,
            freq = "",
            note = MusicalNote.A,
            octive = 0
        )
    )
    val currentInstrument: StateFlow<UsableInstrumentProfile> = _currentInstrument.asStateFlow()

    private val _newInstrument = MutableStateFlow(
        UsableInstrumentProfile(
            name = "",
            icon = R.drawable.radio_button_checked_24px,
            freq = "",
            note = MusicalNote.A,
            octive = 0
        )
    )
    val newInstrument: StateFlow<UsableInstrumentProfile> = _newInstrument.asStateFlow()

    fun updateCurrentName(newName: String) {
        _currentInstrument.update { it.copy(name = newName) }
    }
    fun updateCurrentIcon(newIcon: Int) {
        _currentInstrument.update { it.copy(icon = newIcon) }
    }
    fun updateCurrentFreq(newFreq: String) {
        _currentInstrument.update { it.copy(freq = newFreq) }
    }
    fun updateCurrentNote(newNote: MusicalNote) {
        _currentInstrument.update { it.copy(note = newNote) }
    }
    fun updateCurrentOctive(newOctive: Int) {
        _currentInstrument.update { it.copy(octive = newOctive) }
    }
    fun resetCurrentValues() {
        _currentInstrument.value = UsableInstrumentProfile(
            name = "",
            icon = R.drawable.radio_button_checked_24px,
            freq = "",
            note = MusicalNote.A,
            octive = 0
        )
    }

    fun updateNewName(newName: String) {
        _newInstrument.update { it.copy(name = newName) }
    }
    fun updateNewIcon(newIcon: Int) {
        _newInstrument.update { it.copy(icon = newIcon) }
    }
    fun updateNewFreq(newFreq: String) {
        _newInstrument.update { it.copy(freq = newFreq) }
    }
    fun updateNewNote(newNote: MusicalNote) {
        _newInstrument.update { it.copy(note = newNote) }
    }
    fun updateNewOctive(newOctive: Int) {
        _newInstrument.update { it.copy(octive = newOctive) }
    }
    fun resetNewValues() {
        _newInstrument.value = UsableInstrumentProfile(
            name = "",
            icon = R.drawable.radio_button_checked_24px,
            freq = "",
            note = MusicalNote.A,
            octive = 0
        )
    }
}