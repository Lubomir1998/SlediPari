package com.example.sledipari

import android.app.Application
import com.example.sledipari.api.models.auth.OAuth2Error
import dagger.hilt.android.HiltAndroidApp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@HiltAndroidApp
class ApplicationClass: Application()

val jsonInstance = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

val basicClient = HttpClient(OkHttp) {

    expectSuccess = true
    install(ContentNegotiation) {
        json(jsonInstance)
    }
    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, _ ->
            val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
            val exceptionResponse = clientException.response.body<OAuth2Error>().getException()
            exceptionResponse?.let {
                throw it
            }
        }
    }
    install(HttpTimeout) {
        connectTimeoutMillis = 10_000
        socketTimeoutMillis = 60_000
        requestTimeoutMillis = 60_000
    }
}