package com.averyvi.bilbo.storage

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase

@Dao
interface UserDao {
    @Query("SELECT * FROM instrumentdbrow")
    fun getAll(): List<InstrumentDBRow>


    @Insert
    fun insertAll(instruments: InstrumentDBRow)

    @Delete
    fun delete(instrument: InstrumentDBRow)
}

// Make the database class
@Database(entities = [InstrumentDBRow::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}