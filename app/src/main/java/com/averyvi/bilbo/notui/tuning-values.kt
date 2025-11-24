package com.averyvi.bilbo.notui

data class FirstHarmonic(val name: String, val frequency: Double)

val standardTuning = listOf(
    FirstHarmonic("E", 82.00),
    FirstHarmonic("A", 110.00),
    FirstHarmonic("D", 147.00),
    FirstHarmonic("G", 196.00),
    FirstHarmonic("H", 247.00),
    FirstHarmonic("E", 330.00)
)