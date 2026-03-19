package com.averyvi.bilbo.definitions

import androidx.compose.ui.graphics.Color

class InstrumentStyling(
    val instrumentName: String,
    val instrumentIcon: Int,
)

class InstrumentProfile(
    val refFreq: Int,
    val positionInOctive: Int,
    val refOctive: Int
)

val DefaultInstrumentProfile = InstrumentProfile(
    refFreq = 440,
    positionInOctive = 5,
    refOctive = 4
)