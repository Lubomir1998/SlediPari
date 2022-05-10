package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class PostSpendingRequest(

    val monthId: String,
    val title: String,
    val price: Float
)
