package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class AddHubRequest(
    val email: String,
    val name: String
)
