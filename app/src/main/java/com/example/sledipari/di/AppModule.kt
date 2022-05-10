package com.example.sledipari.di

import android.content.Context
import androidx.room.Room
import com.example.sledipari.utility.Constants.BASE_URL
import com.example.sledipari.utility.Constants.BASE_URL_LOCALHOST
import com.example.sledipari.utility.Constants.USE_LOCALHOST
import com.example.sledipari.api.MonthApi
import com.example.sledipari.data.db.MonthsDatabase
import com.example.sledipari.jsonInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context

    @Singleton
    @Provides
    fun provideMonthApi() =
        MonthApi(HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(jsonInstance)
            }
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

}