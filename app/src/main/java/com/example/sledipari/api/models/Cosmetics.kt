package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Cosmetics(

    var higien: Float = 0.0f,
    var other: Float = 0.0f
)
