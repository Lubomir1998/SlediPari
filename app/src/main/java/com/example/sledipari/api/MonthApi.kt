package com.example.sledipari.api

import android.content.Context
import com.example.sledipari.R
import com.example.sledipari.api.models.ApiResponse
import com.example.sledipari.api.models.CurrencyRatesResponse
import com.example.sledipari.api.models.MonthDTO
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.utility.Constants.EXCHANGE_RATES_API_KEY
import com.example.sledipari.utility.baseUrl
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class MonthApi(private val httpClient: HttpClient, private val context: Context) {

    suspend fun getMonth(month: String): MonthDTO {

        val response = try {
            httpClient.get<ApiResponse<MonthDTO>>(baseUrl() + "getExpense_v2") {
                parameter("month", month)
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response.parse()
    }

    suspend fun postSpending(postRequest: PostSpendingRequest): Boolean {

        val response = try {
            httpClient.post<ApiResponse<Boolean>>(baseUrl() + "addExpense_v2") {
                contentType(ContentType.Application.Json)
                body = postRequest
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response.isSuccess
    }

    suspend fun undoSpending(postRequest: PostSpendingRequest): Boolean {

        val response = try {
            httpClient.post<ApiResponse<Boolean>>(baseUrl() + "removeExpense_v2") {
                contentType(ContentType.Application.Json)
                body = postRequest
            }
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return if (response.isSuccess) {
            true
        } else {
            throw Exception(response.errors)
        }
    }

    suspend fun getAllMonths(): List<MonthDTO> {

        val response = try {
            httpClient.get<ApiResponse<List<MonthDTO>>>( baseUrl() + "getAllMonths_v2")
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.something_went_wrong))
        }

        return response.parse()
    }

    suspend fun getCurrencyRates(): CurrencyRatesResponse {

        return httpClient.get("https://api.apilayer.com/exchangerates_data/latest") {
            parameter("base", "BGN")
            parameter("apikey", EXCHANGE_RATES_API_KEY)
        }
    }
}