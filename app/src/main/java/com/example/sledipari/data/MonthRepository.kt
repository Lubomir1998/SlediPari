package com.example.sledipari.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.sledipari.api.MonthApi
import com.example.sledipari.api.models.ApiResponse
import com.example.sledipari.api.models.MonthDTO
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.data.db.MonthDao
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.CurrencyResponseLocal
import com.example.sledipari.data.models.Transaction
import com.example.sledipari.data.models.mapToRates
import com.example.sledipari.utility.Constants
import com.example.sledipari.utility.Constants.HISTORY_DURATION
import com.example.sledipari.utility.Constants.LAST_MODIFIED_DATE
import com.example.sledipari.utility.extensions.isCurrent
import com.example.sledipari.utility.formatDate
import io.ktor.client.call.*
import io.ktor.http.*
import javax.inject.Inject

class MonthRepository @Inject constructor(
    private val api: MonthApi,
    private val dao: MonthDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getMonthsOnStart() {

        val response = api.checkLastModifiedDate()
        val lastModified = response.headers[HttpHeaders.LastModified]?.toLong() ?: 0L

        Log.d("test dates", "last modified on server ${lastModified.formatDate("dd-MM-yyyy - HH:mm:ss")}, last modified local ${sharedPreferences.getLong(LAST_MODIFIED_DATE, -111L).formatDate("dd-MM-yyyy - HH:mm:ss")}")

        if (sharedPreferences.getLong(LAST_MODIFIED_DATE, 0L) < lastModified) {
            Log.d("test dates", "fetching all months")
            getAllMonths()
        }
    }
    suspend fun getMonth(monthId: String): Month {

        val response = api.getMonth(monthId)
        val month = response.parse().toMonth()
        if (month.isCurrent()) {
            dao.insertMonth(month)
        }
        return dao.getMonth(monthId)
    }

    suspend fun getMonthLocal(monthId: String): Month? {

        return try {
            dao.getMonth(monthId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun postSpending(request: PostSpendingRequest, post: Boolean = true): Boolean {

        return if (post) {

            val response = api.postSpending(request)
            response.body<ApiResponse<Boolean>>().errors?.let {
                throw Exception(it)
            }

            val success = response.body<ApiResponse<Boolean>>().isSuccess
            val lastModified = response.headers[HttpHeaders.LastModified]?.toLong() ?: 0L

            if (success) {
                Log.d("test dates", "saved locally -> ${lastModified.formatDate("dd-MM-yyyy - HH:mm:ss")}")
                sharedPreferences.edit().putLong(LAST_MODIFIED_DATE, lastModified).apply()
            }

            success
        }
        else {

            val response = api.undoSpending(request)
            response.body<ApiResponse<Boolean>>().errors?.let {
                throw Exception(it)
            }

            val success = response.body<ApiResponse<Boolean>>().isSuccess
            val lastModified = response.headers[HttpHeaders.LastModified]?.toLong() ?: 0L

            if (success) {
                Log.d("test dates", "saved locally -> ${lastModified.formatDate("dd-MM-yyyy - HH:mm:ss")}")
                sharedPreferences.edit().putLong(LAST_MODIFIED_DATE, lastModified).apply()
            }

            success
        }
    }

    suspend fun getAllMonthsLocal(): List<Month> {

        return dao.getAllMonths()
    }

    suspend fun getAllMonths() {

        val response = api.getAllMonths()
        val months = response.body<ApiResponse<List<MonthDTO>>>().parse()
        val lastModified = response.headers[HttpHeaders.LastModified]?.toLong() ?: 0L

        sharedPreferences.edit().putLong(LAST_MODIFIED_DATE, lastModified).apply()
        dao.deleteAllMonths()
        for (month in months) {
            dao.insertMonth(month.toMonth())
        }
    }

    suspend fun addTransactionInHistory(transaction: Transaction) {

        dao.insertTransaction(transaction)
    }

    suspend fun getHistory(): List<Transaction> {

        return dao.getAllTransactions()
    }

    suspend fun deleteSomeHistory() {

        try {

            val history = getHistory()
            val currentDate = System.currentTimeMillis()

            for (transaction in history) {

                if (currentDate - transaction.timestamp > HISTORY_DURATION) {

                    try {
                        dao.deleteTransaction(transaction.timestamp)
                    } catch (e: Exception) {
                        continue
                    }
                }
            }

        } catch (e: Exception) { }
    }

    suspend fun saveCurrencyRates() {

        val currencyRatesResponse = api.getCurrencyRates()

        val rates = currencyRatesResponse.mapToRates()

        dao.deleteOldRates()
        dao.insertRates(rates)
    }

    suspend fun getRates(): CurrencyResponseLocal? {

        return dao.getRates().firstOrNull()
    }
}