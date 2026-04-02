package com.averyvi.bilbo.data.uiState

import com.averyvi.bilbo.R
import com.averyvi.bilbo.data.storage.InstrumentDBRow
import com.averyvi.bilbo.definitions.MusicalNote
import kotlin.text.takeLast

data class UsableInstrumentProfile(
    var name:String = "Piano",
    var icon: Int = R.drawable.radio_button_checked_24px,
    var freq: String = "0",
    var note: MusicalNote = MusicalNote.A,
    var octive: Int = 0,
) {
    fun DBRowToUsable(row: InstrumentDBRow){
        name = row.instrumentName
        icon = row.instrumentIcon
        freq = row.refFreq.toString()
        note = MusicalNote.entries[row.positionInOctive - 1]
        octive = row.refOctive
    }
}