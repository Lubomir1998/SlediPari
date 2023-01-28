package com.example.sledipari.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sledipari.api.models.CurrencyRatesResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity
data class CurrencyResponseLocal(
    @PrimaryKey
    val timestamp: Long,
    val rates: String
)

fun CurrencyRatesResponse.mapToRates(): CurrencyResponseLocal {
    return CurrencyResponseLocal(this.timestamp * 1000, Json.encodeToString(this.rates))
}

fun String.toMap(): Map<String, Double> {

    return Json.decodeFromString(this)
}