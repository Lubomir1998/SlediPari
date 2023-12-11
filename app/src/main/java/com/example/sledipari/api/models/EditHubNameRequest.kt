package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class EditHubNameRequest(
    val name: String,
    val hubId: String
)
