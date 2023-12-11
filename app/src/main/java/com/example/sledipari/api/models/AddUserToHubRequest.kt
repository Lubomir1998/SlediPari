package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class AddUserToHubRequest(
    val email: String,
    val hubId: String
)
