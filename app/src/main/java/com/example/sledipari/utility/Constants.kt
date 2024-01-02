package com.example.sledipari.utility

import com.example.sledipari.BuildConfig

object Constants {

    const val USE_LOCALHOST = false

    const val BASE_URL = BuildConfig.BASE_URL
    const val BASE_URL_LOCALHOST = BuildConfig.BASE_URL_LOCALHOST

    const val CLIENT_ID = BuildConfig.CLIENT_ID
    const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET
    const val DOMAIN = BuildConfig.DOMAIN

    const val ENCRYPTED_SHARED_PREFS_NAME = "ENCRYPTED_SHARED_PREFS_NAME"
    const val KEY_FIREBASE_TOKEN = "KEY_FIREBASE_TOKEN"
    const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"

    const val SERVER_KEY = BuildConfig.SERVER_KEY
    const val SENDER_ID = BuildConfig.SENDER_ID
    const val EXCHANGE_RATES_API_KEY = BuildConfig.EXCHANGE_RATES_API_KEY

    const val CHANNEL_ID = "CHANNEL_ID"

    val HISTORY_DURATION = if (USE_LOCALHOST) {
        1000L * 60 * 5
    } else {
        1000L * 60 * 60 * 24 * 7 * 2
    }

    val DELETE_HISTORY_INTERVAL = if (USE_LOCALHOST) {
        1000L * 60 * 2
    } else {
        1000L * 60 * 60 * 24
    }

    const val HISTORY_TIMESTAMP = "history_timestamp"

    const val GET_RATES_INTERVAL = 1000L * 60 * 60 * 24
    const val BASE_CURRENCY_KEY = "base_currency_key"

    const val LAST_MODIFIED_DATE = "last_modified_date"
}