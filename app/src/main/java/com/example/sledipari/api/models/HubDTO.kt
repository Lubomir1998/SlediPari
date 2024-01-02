package com.example.sledipari.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HubDTO(
    val name: String,
    val users: List<String>,
    val owner: String,
    @SerialName("uid") val id: String
)
