package com.averyvi.bilbo.data.uiState

import com.averyvi.bilbo.R
import com.averyvi.bilbo.data.storage.InstrumentDBRow
import com.averyvi.bilbo.definitions.MusicalNote
import kotlin.text.takeLast

data class UsableInstrumentProfile(
    val name:String = "Piano",
    val icon: Int = R.drawable.radio_button_checked_24px,
    val freq: String = "0",
    val note: MusicalNote = MusicalNote.A,
    val octive: Int = 0,
) {
    companion object {
        fun fromDBRow(row: InstrumentDBRow): UsableInstrumentProfile {
            return UsableInstrumentProfile(
                name = row.instrumentName,
                icon = row.instrumentIcon,
                freq = row.refFreq.toString(),
                note = MusicalNote.entries[row.positionInOctive],
                octive = row.refOctive
            )
        }
    }
}