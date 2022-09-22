package com.example.sledipari.api

import com.example.sledipari.api.models.pushnotifications.PushNotification
import com.example.sledipari.utility.Constants.SENDER_ID
import com.example.sledipari.utility.Constants.SERVER_KEY
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class FirebasePushNotificationsApi(private val client: HttpClient) {

    suspend fun sendPushNotification(pushNotification: PushNotification) {

        client.post<HttpResponse> {
            url("https://fcm.googleapis.com/fcm/send")
            contentType(ContentType.Application.Json)
            header("Authorization", "key=$SERVER_KEY")
            body = pushNotification
        }
    }
}