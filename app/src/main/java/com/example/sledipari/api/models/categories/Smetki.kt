package com.example.sledipari.api.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class Smetki(

    var tok: Float = 0.0f,
    var voda: Float = 0.0f,
    var toplo: Float = 0.0f,
    var internet: Float = 0.0f,
    var vhod: Float = 0.0f,
    var telefon: Float = 0.0f
)
