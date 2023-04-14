package com.example.sledipari.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.sledipari.api.FirebasePushNotificationsApi
import com.example.sledipari.api.MonthApi
import com.example.sledipari.data.db.MonthsDatabase
import com.example.sledipari.jsonInstance
import com.example.sledipari.utility.Constants.ENCRYPTED_SHARED_PREFS_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
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
    fun provideMonthApi(@ApplicationContext context: Context) =
        MonthApi(HttpClient(CIO) {
            install(ContentNegotiation) {
                json(jsonInstance)
            }
        }, context)

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
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}