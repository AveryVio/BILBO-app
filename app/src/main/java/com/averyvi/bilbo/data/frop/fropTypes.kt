package com.averyvi.bilbo.data.frop

sealed class FropMessage

data class TuningData(
    val octive: Int,
    val positionInOctave: Int,
    val frequency: Int,
    val difference: Int
) : FropMessage()