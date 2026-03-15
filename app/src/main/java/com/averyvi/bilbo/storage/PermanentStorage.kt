package com.averyvi.bilbo.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class InstrumentProfile(
    val refFreq: Int,
    val positionInOctive: Int,
    val refOctive: Int
)

@Entity
class InstrumentDBRow(
    @PrimaryKey(autoGenerate = false) val instrumentName:String = "Piano",
    @ColumnInfo(name = "InstrumentIcon") val instrumentIcon: Int = 0,
    @ColumnInfo(name = "InstrumentThemeColor") val instrumentThemeColor: Int = 0,
    @ColumnInfo(name = "refFreq") val refFreq: Int = 0,
    @ColumnInfo(name = "positionInOctive") val positionInOctive: Int = 0,
    @ColumnInfo(name = "refOctive") val refOctive: Int = 0,
)