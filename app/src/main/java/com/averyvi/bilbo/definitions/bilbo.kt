package com.averyvi.bilbo.definitions

data class FirstHarmonic(val name: String, val frequency: Double)

enum class MusicalNote(var noteNumber: Int) {
    C(1),
    Cs(2),
    D(3),
    Ds(4),
    E(5),
    F(6),
    Fs(7),
    G(8),
    Gs(9),
    A(10),
    As(11),
    H(12),
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