package com.example.sledipari.data.db

import androidx.room.*
import com.example.sledipari.data.models.Month

@Dao
interface MonthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonth(month: Month)

    @Query("SELECT * FROM Month WHERE id = :month")
    suspend fun getMonth(month: String): Month

    @Query("SELECT * FROM Month ORDER BY id ASC")
    suspend fun getAllMonths(): List<Month>
}