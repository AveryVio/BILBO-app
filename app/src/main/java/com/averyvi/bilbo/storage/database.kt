package com.averyvi.bilbo.storage

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import com.averyvi.bilbo.R
import kotlin.concurrent.thread

@Dao
interface UserDao {
    @Query("SELECT * FROM instrumentdbrow")
    fun getAll(): List<InstrumentDBRow>

    @Query("SELECT * FROM instrumentdbrow ORDER BY ID DESC LIMIT 1")
    fun getLast(): InstrumentDBRow

    @Insert
    fun insertAll(instrument: InstrumentDBRow)

    @Delete
    fun delete(instrument: InstrumentDBRow)
}

// Make the database class
@Database(entities = [InstrumentDBRow::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

fun getAllInstruments(dbdao: UserDao): SnapshotStateList<InstrumentDBRow> {
    val outInstruments: SnapshotStateList<InstrumentDBRow> = SnapshotStateList()
    var fromDBInstruments: List<InstrumentDBRow> = emptyList()

    thread {
        fromDBInstruments = dbdao.getAll()
    }.join()
    if(fromDBInstruments.isNotEmpty()) {
        for (instrument in fromDBInstruments) {
            outInstruments.add(
                InstrumentDBRow(
                    instrumentName = instrument.instrumentName,
                    instrumentIcon = instrument.instrumentIcon,
                    refFreq = instrument.refFreq,
                    positionInOctive = instrument.positionInOctive,
                    refOctive = instrument.refOctive,
                )
            )
        }
        return outInstruments
    }
    else return SnapshotStateList()
}

fun deleteAllInstruments(dbdao: UserDao) {
    var fromDBInstruments: List<InstrumentDBRow> = emptyList()

    thread {
        fromDBInstruments = dbdao.getAll()
    }.join()
    if(fromDBInstruments.isNotEmpty()) {
        for (instrument in fromDBInstruments) {
            fromDBInstruments.forEach { dbdao.delete(it) }
        }
    }
}

fun deleteLastInstrument(dbdao: UserDao) {
    var lastInstrument: InstrumentDBRow = InstrumentDBRow(
        instrumentName = "Piano",
        instrumentIcon = R.drawable.radio_button_checked_24px,
        refFreq = 440,
        positionInOctive = 9,
        refOctive = 4,
    )

    thread {
        lastInstrument = dbdao.getLast()
    }.join()
    dbdao.delete(lastInstrument)
}