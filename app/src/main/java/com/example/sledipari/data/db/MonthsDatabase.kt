package com.example.sledipari.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sledipari.data.models.Month

@Database(entities = [Month::class], version = 2)
abstract class MonthsDatabase: RoomDatabase() {

    abstract fun getDao(): MonthDao
}