package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Preparati(

    var clean: Float = 0.0f,
    var wash: Float = 0.0f
)
