package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class HubDTO(
    val name: String,
    val users: List<String>,
    val uid: String
)
