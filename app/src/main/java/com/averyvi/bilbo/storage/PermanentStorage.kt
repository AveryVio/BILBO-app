package com.averyvi.bilbo.storage

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class InstrumentProfile(
    val refFreq: Int,
    val positionInOctive: Int,
    val refOctive: Int
)

@Entity
data class InstrumentDBRow(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "instrumentName") val instrumentName:String = "Piano",
    @ColumnInfo(name = "instrumentIcon") val instrumentIcon: Int = 0,
    @ColumnInfo(name = "refFreq") val refFreq: Int = 0,
    @ColumnInfo(name = "positionInOctive") val positionInOctive: Int = 0,
    @ColumnInfo(name = "refOctive") val refOctive: Int = 0,
)