package com.example.sledipari

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.serialization.json.Json

@HiltAndroidApp
class ApplicationClass: Application()

val jsonInstance = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}