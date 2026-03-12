package com.averyvi.bilbo.definitions

data class FirstHarmonic(val name: String, val frequency: Double)

enum class MusicalNote(noteNumber: Int, noteString: String) {
    C(1,"C"),
    D(2, "D"),
    E(3, "E"),
    F(4, "F"),
    G(5, "G"),
    A(6, "A"),
    H(7, "H"),
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