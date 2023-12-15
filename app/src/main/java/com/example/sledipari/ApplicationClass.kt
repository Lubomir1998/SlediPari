package com.example.sledipari

import android.app.Application
import com.example.sledipari.api.models.auth.OAuth2Error
import com.example.sledipari.api.models.auth.TokenInfo
import com.example.sledipari.utility.Constants.DOMAIN
import dagger.hilt.android.HiltAndroidApp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
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

suspend fun getTokens(token: String): BearerTokens {

    val tokenInfo: TokenInfo = basicClient.submitForm(
        url = "https://$DOMAIN/oauth/token",
        formParameters = Parameters.build {
            append("grant_type", "refresh_token")
            append("refresh_token", token)
        }
    ).body()

    return BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken ?: "")
}