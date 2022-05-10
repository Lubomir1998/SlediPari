package com.example.sledipari.api

import com.example.sledipari.api.models.MonthDTO
import com.example.sledipari.api.models.PostSpendingRequest
import com.example.sledipari.utility.Constants
import com.example.sledipari.utility.Constants.BASE_URL
import com.example.sledipari.utility.Constants.BASE_URL_LOCALHOST
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class MonthApi(private val httpClient: HttpClient) {

    suspend fun getMonth(month: String): MonthDTO {

        return httpClient.get(BASE_URL + "getExpense") {
            parameter("month", month)
        }
    }

    suspend fun postSpending(postRequest: PostSpendingRequest): Boolean {

        val response = httpClient.post<Boolean>(BASE_URL + "addExpense") {
            contentType(ContentType.Application.Json)
            body = postRequest
        }

        return response
    }

    suspend fun getAllMonths(): List<MonthDTO> {

        return httpClient.get( BASE_URL + "getAllMonths")
    }

}