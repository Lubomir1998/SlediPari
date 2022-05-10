package com.example.sledipari.data

import android.content.Context
import com.example.sledipari.R
import com.example.sledipari.utility.Resource
import com.example.sledipari.api.MonthApi
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.data.db.MonthDao
import com.example.sledipari.data.models.Month
import com.example.sledipari.utility.formatDate
import com.example.sledipari.utility.isCurrent
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

    suspend fun getCurrentMonthLocal(): Month? {
        return try {
            dao.getMonth(System.currentTimeMillis().formatDate("MM-yyyy"))
        } catch (e: Exception) {
            null
        }
    }

    suspend fun postSpending(request: PostSpendingRequest): Resource<Boolean> {

        return try {
            Resource.Success(api.postSpending(request))
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: context.getString(R.string.something_went_wrong))
        }
    }

    suspend fun getAllMonths(): Resource<List<Month>> {

        return try {
            val months = dao.getAllMonths()
            Resource.Success(months)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: context.getString(R.string.something_went_wrong))
        }
    }

    suspend fun restoreAllMonths(): Resource<Boolean> {

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

}