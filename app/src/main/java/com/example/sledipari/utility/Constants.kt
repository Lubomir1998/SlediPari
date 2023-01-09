package com.example.sledipari.utility

import com.example.sledipari.BuildConfig

object Constants {

    const val USE_LOCALHOST = true

    const val BASE_URL = BuildConfig.BASE_URL
    const val BASE_URL_LOCALHOST = BuildConfig.BASE_URL_LOCALHOST

    const val ENCRYPTED_SHARED_PREFS_NAME = "ENCRYPTED_SHARED_PREFS_NAME"
    const val KEY_FIREBASE_TOKEN = "KEY_FIREBASE_TOKEN"

    const val SLEDI_PARI_TOPIC = "SLEDI_PARI_TOPIC"
    const val SERVER_KEY = BuildConfig.SERVER_KEY
    const val SENDER_ID = BuildConfig.SENDER_ID
    const val EXCHANGE_RATES_API_KEY = BuildConfig.EXCHANGE_RATES_API_KEY

    const val CHANNEL_ID = "CHANNEL_ID"

    val HISTORY_DURATION = if (USE_LOCALHOST) {
        1000L * 60 * 5
    } else {
        1000L * 60 * 60 * 24 * 7
    }

    val DELETE_HISTORY_INTERVAL = if (USE_LOCALHOST) {
        1000L * 60 * 2
    } else {
        1000L * 60 * 60 * 24
    }

    const val HISTORY_TIMESTAMP = "history_timestamp"

    const val GET_RATES_INTERVAL = 1000L * 60 * 60 * 6
    const val RATES_TIMESTAMP = "rates_timestamp"
}