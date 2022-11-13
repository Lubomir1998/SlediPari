package com.example.sledipari.data.db

import androidx.room.*
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.Transaction

@Dao
interface MonthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonth(month: Month)

    @Query("SELECT * FROM Month WHERE id = :month")
    suspend fun getMonth(month: String): Month

    @Query("SELECT * FROM Month ORDER BY id ASC")
    suspend fun getAllMonths(): List<Month>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<Transaction>

    @Query("DELETE FROM `Transaction` WHERE timestamp = :date")
    suspend fun deleteTransaction(date: Long)
}