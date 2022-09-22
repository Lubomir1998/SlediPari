package com.example.sledipari.api.models.categories

import kotlinx.serialization.Serializable

@Serializable
data class Frizior(
    var friziorSub: Float = 0.0f,
    var cosmetic: Float = 0.0f,
    var manikior: Float = 0.0f
)
