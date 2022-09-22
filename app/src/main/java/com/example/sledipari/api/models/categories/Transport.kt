package com.example.sledipari.api.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class Transport(

    var public: Float = 0.0f,
    var taxi: Float = 0.0f,
    var car: Float = 0.0f
)
