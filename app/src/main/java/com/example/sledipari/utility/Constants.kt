package com.example.sledipari.utility

import com.example.sledipari.BuildConfig

object Constants {

    const val USE_LOCALHOST = true

    const val BASE_URL = "https://desolate-chamber-91023.herokuapp.com/"
    const val BASE_URL_LOCALHOST = "http://192.168.0.105:1926/" // home

    const val ENCRYPTED_SHARED_PREFS_NAME = "ENCRYPTED_SHARED_PREFS_NAME"
    const val KEY_FIREBASE_TOKEN = "KEY_FIREBASE_TOKEN"

    const val SLEDI_PARI_TOPIC = "SLEDI_PARI_TOPIC"
    const val SERVER_KEY = BuildConfig.SERVER_KEY
    const val SENDER_ID = BuildConfig.SENDER_ID

    const val CHANNEL_ID = "CHANNEL_ID"
}