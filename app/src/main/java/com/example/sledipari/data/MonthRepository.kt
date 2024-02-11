package com.example.sledipari.data

import android.content.SharedPreferences
import com.example.sledipari.api.MonthApi
import com.example.sledipari.api.models.AddHubRequest
import com.example.sledipari.api.models.AddUserToHubRequest
import com.example.sledipari.api.models.ApiResponse
import com.example.sledipari.api.models.CreateHubResponse
import com.example.sledipari.api.models.EditHubNameRequest
import com.example.sledipari.api.models.HubDTO
import com.example.sledipari.api.models.MonthDTO
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.api.models.UserDTO
import com.example.sledipari.data.db.MonthDao
import com.example.sledipari.data.models.Month
import com.example.sledipari.data.models.CurrencyResponseLocal
import com.example.sledipari.data.models.Hub
import com.example.sledipari.data.models.Transaction
import com.example.sledipari.data.models.mapToRates
import com.example.sledipari.utility.Constants.HISTORY_DURATION
import com.example.sledipari.utility.Constants.LAST_MODIFIED_DATE
import com.example.sledipari.utility.extensions.isCurrent
import io.ktor.client.call.*
import io.ktor.http.*
import javax.inject.Inject

class MonthRepository @Inject constructor(
    private val api: MonthApi,
    private val dao: MonthDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getMonthsOnStart(hubId: String) {

        val response = api.checkLastModifiedDate()
        val lastModified = response.headers[HttpHeaders.LastModified]?.toLong() ?: 0L

        if (sharedPreferences.getLong(LAST_MODIFIED_DATE, -1L) < lastModified) {
            getAllMonths(hubId)
        }
    }
    suspend fun getMonth(monthId: String, hubId: String): Month {

        val response = api.getMonth(monthId)
        val month = response.parse().toMonth()
        if (month.isCurrent()) {
            dao.insertMonth(month)
        }
        return dao.getMonth(monthId, hubId)
    }

    suspend fun getMonthLocal(monthId: String, hubId: String): Month? {

        return try {
            dao.getMonth(monthId, hubId)
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

            response.headers[HttpHeaders.LastModified]?.toLong()?.let { lastModified ->
                sharedPreferences.edit().putLong(LAST_MODIFIED_DATE, lastModified).apply()
            }

            response.body<ApiResponse<Boolean>>().isSuccess
        }
        else {

            val response = api.undoSpending(request)
            response.body<ApiResponse<Boolean>>().errors?.let {
                throw Exception(it)
            }

            response.headers[HttpHeaders.LastModified]?.toLong()?.let { lastModified ->
                sharedPreferences.edit().putLong(LAST_MODIFIED_DATE, lastModified).apply()
            }

            response.body<ApiResponse<Boolean>>().isSuccess
        }
    }

    suspend fun getAllMonthsLocal(): List<Month> {

        return dao.getAllMonths()
    }

    suspend fun getAllMonths(hubId: String): List<Month> {

        val response = api.getAllMonths(hubId)
        val months = response.body<ApiResponse<List<MonthDTO>>>().parse()

        response.headers[HttpHeaders.LastModified]?.toLong()?.let { lastModified ->

            sharedPreferences.edit().putLong(LAST_MODIFIED_DATE, lastModified).apply()
        }

        for (month in months) {
            dao.insertMonth(month.toMonth())
        }

        return getAllMonthsLocal()
    }

    suspend fun createHub(request: AddHubRequest) {

        val response = api.createHub(request)
        val hubId = response.body<ApiResponse<CreateHubResponse>>().parse()


    }

    suspend fun getAllHubsForUser(): List<Hub> {

        val response = api.getAllHubsForUser()
        val hubs = response.body<ApiResponse<List<HubDTO>>>().parse()

        for (dto in hubs) {

            val hub = dto.toHub()
            dao.insertHub(hub)
        }

        return getHubsLocal()
    }

    private suspend fun getHubsLocal(): List<Hub> {

        return dao.getAllHubs()
    }

    suspend fun editHubName(request: EditHubNameRequest): Boolean {

        val response = api.editHubName(request)
        return response.body<ApiResponse<Boolean>>().isSuccess
    }

    suspend fun addUserToHub(request: AddUserToHubRequest): Boolean {

        val response = api.addUserToHub(request)
        return response.body<ApiResponse<Boolean>>().isSuccess
    }

    suspend fun getUserByEmail(email: String?): UserDTO {

        val response = api.getUserByEmail(email)
        val user = response.body<ApiResponse<UserDTO>>().parse()
        return user
    }

    suspend fun getUserById(id: String?): UserDTO {

        val response = api.getUserById(id)
        val user = response.body<ApiResponse<UserDTO>>().parse()
        return user
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