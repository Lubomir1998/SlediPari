package com.example.sledipari.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.CurrencyResponseLocal
import com.example.sledipari.data.models.Hub
import com.example.sledipari.data.models.Transaction

@Database(
    entities = [
        Month::class,
        Transaction::class,
        CurrencyResponseLocal::class,
        Hub::class
    ],
    version = 6
)
abstract class MonthsDatabase : RoomDatabase() {

    abstract fun getDao(): MonthDao
}