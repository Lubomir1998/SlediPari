package com.example.sledipari.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.Rates
import com.example.sledipari.data.models.Transaction

@Database(
    entities = [
        Month::class,
        Transaction::class,
        Rates::class
    ],
    version = 5
)
abstract class MonthsDatabase : RoomDatabase() {

    abstract fun getDao(): MonthDao
}