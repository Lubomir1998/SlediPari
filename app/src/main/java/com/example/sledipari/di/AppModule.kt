package com.example.sledipari.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.sledipari.api.FirebasePushNotificationsApi
import com.example.sledipari.api.MonthApi
import com.example.sledipari.api.models.auth.TokenInfo
import com.example.sledipari.basicClient
import com.example.sledipari.data.db.MonthsDatabase
import com.example.sledipari.jsonInstance
import com.example.sledipari.utility.Constants.ENCRYPTED_SHARED_PREFS_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context



    @Singleton
    @Provides
    fun provideFcmApi() =
        FirebasePushNotificationsApi(HttpClient(CIO) {
            expectSuccess = false
        })

    @Singleton
    @Provides
    fun provideSpendingsDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            MonthsDatabase::class.java,
            "sledi_pari_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDao(db: MonthsDatabase) = db.getDao()

    @Singleton
    @Provides
    fun provideMonthApi(@ApplicationContext context: Context, sharedPreferences: SharedPreferences) =
        MonthApi(HttpClient(CIO) {
            install(ContentNegotiation) {
                json(jsonInstance)
            }
            install(Auth) {
                bearer {
                    loadTokens {
//                        val tokenInfo: TokenInfo = basicClient.submitForm(
//                            url = "https://dev-j6hq26y1j8pv5deu.us.auth0.com/oauth/token",
//                            formParameters = Parameters.build {
//                                append("grant_type", "authorization_code")
//                                append("client_id", "wQHnVE7ocP1SOZux0oVRsQm5RGKkiFPX")
//                                append("client_secret", "n8JnRL6WyTTe-xreqPUMiZ4Kc62h7TRsjn6tPAcTGejzQl-XI5WPiJLAmT0Vnbm6")
//                                append("redirect_uri", "app://dev-j6hq26y1j8pv5deu.us.auth0.com/android/com.example.sledipari/callback,")
//                            }
//                        ).body()
                        val token = sharedPreferences.getString("token", "") ?: ""
                        val tokenInfo: TokenInfo = basicClient.submitForm(
                            url = "https://dev-j6hq26y1j8pv5deu.us.auth0.com/oauth/token",
                            formParameters = Parameters.build {
                                append("grant_type", "refresh_token")
                                append("refresh_token", token)
                            }
                        ).body()
                        BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken ?: "")
                    }
                    refreshTokens {

                        val token = oldTokens?.refreshToken ?: sharedPreferences.getString("token", "") ?: ""
                        val tokenInfo: TokenInfo = basicClient.submitForm(
                            url = "https://dev-j6hq26y1j8pv5deu.us.auth0.com/oauth/token",
                            formParameters = Parameters.build {
                                append("grant_type", "refresh_token")
                                append("refresh_token", token)
                            }
                        ).body()
                        BearerTokens(tokenInfo.accessToken, tokenInfo.refreshToken ?: "")
                    }
                    sendWithoutRequest { request ->
                        request.url.host == "https://api.apilayer.com/exchangerates_data/latest"
                    }
                }
            }
            defaultRequest {
                val token = sharedPreferences.getString("token", null)
                token?.let {
                    headers {
                        append("Authorization", "Bearer $it")
                    }
                }
            }
        }, context)

    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {

        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            ENCRYPTED_SHARED_PREFS_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}