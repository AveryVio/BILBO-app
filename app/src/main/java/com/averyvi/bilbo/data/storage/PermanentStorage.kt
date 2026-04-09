package com.averyvi.bilbo.data.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InstrumentDBRow(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "instrumentName") val instrumentName:String = "Piano",
    @ColumnInfo(name = "instrumentIcon") val instrumentIcon: Int = 0,
    @ColumnInfo(name = "refFreq") val refFreq: Int = 0,
    @ColumnInfo(name = "positionInOctive") val positionInOctive: Int = 0,
    @ColumnInfo(name = "refOctive") val refOctive: Int = 0,
)