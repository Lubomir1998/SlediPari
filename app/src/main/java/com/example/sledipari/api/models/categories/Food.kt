package com.example.sledipari.api.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class Food(

    var home: Float = 0.0f,
    var restaurant: Float = 0.0f
)
