package com.example.sledipari.api

import android.content.Context
import com.example.sledipari.R
import com.example.sledipari.api.models.AddHubRequest
import com.example.sledipari.api.models.AddUserToHubRequest
import com.example.sledipari.api.models.ApiResponse
import com.example.sledipari.api.models.CurrencyRatesResponse
import com.example.sledipari.api.models.EditHubNameRequest
import com.example.sledipari.api.models.MonthDTO
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.utility.Constants.EXCHANGE_RATES_API_KEY
import com.example.sledipari.utility.baseUrl
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MonthApi(private val httpClient: HttpClient, private val context: Context) {

    suspend fun getMonth(month: String): ApiResponse<MonthDTO> {

        val response = try {
            httpClient.get(baseUrl() + "getExpense_v3") {
                parameter("month", month)
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response.body()
    }

    suspend fun postSpending(postRequest: PostSpendingRequest): HttpResponse {

        val response = try {
            httpClient.post(baseUrl() + "addExpense_v3") {
                contentType(ContentType.Application.Json)
                setBody(postRequest)
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response
    }

    suspend fun undoSpending(postRequest: PostSpendingRequest): HttpResponse {

        val response = try {
            httpClient.post(baseUrl() + "removeExpense_v3") {
                contentType(ContentType.Application.Json)
                setBody(postRequest)
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response
    }

    suspend fun getAllMonths(): HttpResponse {

        val response = try {
            httpClient.get( baseUrl() + "getAllMonths_v3")
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response
    }

    suspend fun getAllHubsForUser(): HttpResponse {

        val response = try {
            httpClient.get(baseUrl() + "getHubsForEmail")
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response
    }

    suspend fun createHub(request: AddHubRequest): HttpResponse {

        val response = try {
            httpClient.post(baseUrl() + "createHub") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response
    }

    suspend fun addUserToHub(request: AddUserToHubRequest): HttpResponse {

        val response = try {
            httpClient.post(baseUrl() + "addUserToHub") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response
    }

    suspend fun editHubName(request: EditHubNameRequest): HttpResponse {

        val response = try {
            httpClient.post(baseUrl() + "editHubName") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response
    }

    suspend fun checkLastModifiedDate(): HttpResponse {

        val response = try {
            httpClient.get(baseUrl() + "getMonthsCheckDate_v2")
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response
    }

    suspend fun getCurrencyRates(): CurrencyRatesResponse {

        val response = httpClient.get("https://api.apilayer.com/exchangerates_data/latest") {
            parameter("base", "BGN")
            parameter("apikey", EXCHANGE_RATES_API_KEY)
        }

        return response.body()
    }
}