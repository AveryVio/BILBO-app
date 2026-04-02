package com.averyvi.bilbo.definitions

data class FirstHarmonic(val name: String, val frequency: Double)

enum class MusicalNote(var noteNumber: Int) {
    C(1),
    D(2),
    E(3),
    F(4),
    G(5),
    A(6),
    H(7),
}

/*
THE USER SEES TODO
    octives (just numbers - ok)
    notes (use enum - check usage)
    cents (just numbers - ok)
    freq (just numbers - ok)
ACTIONS TODO
    cents to an offset value
 */

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