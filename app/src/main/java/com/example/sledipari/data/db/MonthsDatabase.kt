package com.example.sledipari.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
    version = 7
)
@TypeConverters(
    ListConverter::class
)
abstract class MonthsDatabase : RoomDatabase() {

    abstract fun getDao(): MonthDao
}