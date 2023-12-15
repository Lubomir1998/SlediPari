package com.example.sledipari.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.sledipari.api.FirebasePushNotificationsApi
import com.example.sledipari.api.MonthApi
import com.example.sledipari.data.db.MonthsDatabase
import com.example.sledipari.getTokens
import com.example.sledipari.jsonInstance
import com.example.sledipari.utility.Constants.ENCRYPTED_SHARED_PREFS_NAME
import com.example.sledipari.utility.Constants.KEY_REFRESH_TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.*
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
        MonthApi(HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(jsonInstance)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val token = sharedPreferences.getString(KEY_REFRESH_TOKEN, "") ?: ""
                        getTokens(token)
                    }
                    refreshTokens {

                        val token = oldTokens?.refreshToken ?: sharedPreferences.getString(KEY_REFRESH_TOKEN, "") ?: ""
                        getTokens(token)
                    }
                    sendWithoutRequest { request ->
                        request.url.host == "https://api.apilayer.com/exchangerates_data/latest"
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