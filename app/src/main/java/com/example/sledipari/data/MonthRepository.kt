package com.example.sledipari.data

import android.content.Context
import com.example.sledipari.api.MonthApi
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.data.db.MonthDao
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.CurrencyResponseLocal
import com.example.sledipari.data.models.Transaction
import com.example.sledipari.data.models.mapToRates
import com.example.sledipari.utility.Constants.HISTORY_DURATION
import com.example.sledipari.utility.extensions.isCurrent
import javax.inject.Inject

class MonthRepository @Inject constructor(
    private val api: MonthApi,
    private val dao: MonthDao,
    private val context: Context,
) {

    suspend fun getMonth(monthId: String): Month {

        val month = api.getMonth(monthId).toMonth()
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
            api.postSpending(request)
        }
        else {
            api.undoSpending(request)
        }
    }

    suspend fun getAllMonthsLocal(): List<Month> {

        return dao.getAllMonths()
    }

    suspend fun getAllMonths() {

        val months = api.getAllMonths()

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