package com.example.sledipari.data.db

import androidx.room.*
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.CurrencyResponseLocal
import com.example.sledipari.data.models.Hub
import com.example.sledipari.data.models.Transaction

@Dao
interface MonthDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonth(month: Month)

    @Query("SELECT * FROM Month WHERE id = :month AND hubId = :hubId")
    suspend fun getMonth(month: String, hubId: String): Month

    @Query("SELECT * FROM Month ORDER BY id ASC")
    suspend fun getAllMonths(): List<Month>

    @Query("DELETE FROM `Month`")
    suspend fun deleteAllMonths()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<Transaction>

    @Query("DELETE FROM `Transaction` WHERE timestamp = :date")
    suspend fun deleteTransaction(date: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(currencyResponseLocal: CurrencyResponseLocal)

    @Query("SELECT * FROM CurrencyResponseLocal")
    suspend fun getRates(): List<CurrencyResponseLocal>

    @Query("DELETE FROM CurrencyResponseLocal")
    suspend fun deleteOldRates()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHub(hub: Hub)

    @Query("SELECT * FROM Hub")
    suspend fun getAllHubs(): List<Hub>
}