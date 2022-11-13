package com.example.sledipari.data

import android.content.Context
import com.example.sledipari.R
import com.example.sledipari.api.MonthApi
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.data.db.MonthDao
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.Transaction
import com.example.sledipari.utility.Constants.HISTORY_DURATION
import com.example.sledipari.utility.Resource
import com.example.sledipari.utility.extensions.isCurrent
import javax.inject.Inject

class MonthRepository @Inject constructor(
    private val api: MonthApi,
    private val dao: MonthDao,
    private val context: Context
) {

    suspend fun getMonth(monthId: String): Resource<Month> {

        return try {
            val month = api.getMonth(monthId).toMonth()
            if (month.isCurrent()) {
                dao.insertMonth(month)
            }
            Resource.Success(dao.getMonth(monthId))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: context.getString(R.string.something_went_wrong))
        }
    }

    suspend fun getMonthLocal(monthId: String): Month? {

        return try {
            dao.getMonth(monthId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun postSpending(request: PostSpendingRequest, post: Boolean = true): Resource<Boolean> {

        return try {
            if (post) {
                Resource.Success(api.postSpending(request))
            }
            else {
                Resource.Success(api.undoSpending(request))
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: context.getString(R.string.something_went_wrong))
        }
    }

    suspend fun getAllMonthsLocal(): Resource<List<Month>> {

        return try {
            val months = dao.getAllMonths()
            Resource.Success(months)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: context.getString(R.string.something_went_wrong))
        }
    }

    suspend fun getAllMonths(): Resource<Boolean> {

        return try {
            val months = api.getAllMonths()

            for (month in months) {
                dao.insertMonth(month.toMonth())
            }

            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: context.getString(R.string.something_went_wrong))
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

}