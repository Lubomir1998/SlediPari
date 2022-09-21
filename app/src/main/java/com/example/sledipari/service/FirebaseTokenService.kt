package com.example.sledipari.service

import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseTokenService: FirebaseMessagingService() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onNewToken(token: String) {
        super.onNewToken(token)


    }
}