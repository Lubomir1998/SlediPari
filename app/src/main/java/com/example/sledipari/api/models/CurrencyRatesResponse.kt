package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyRatesResponse(
    val success: Boolean,
    val rates: RatesDTO
)
