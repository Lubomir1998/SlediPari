package com.example.sledipari.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Month(
    var clothes: Float = 0.0f,
    var workout: Float = 0.0f,
    var remont: Float = 0.0f,
    var food: Float = 0.0f,
    var home: Float = 0.0f,
    var restaurant: Float = 0.0f,
    var smetki: Float = 0.0f,
    var tok: Float = 0.0f,
    var voda: Float = 0.0f,
    var toplo: Float = 0.0f,
    var internet: Float = 0.0f,
    var vhod: Float = 0.0f,
    var telefon: Float = 0.0f,
    var transport: Float = 0.0f,
    var publicT: Float = 0.0f,
    var taxi: Float = 0.0f,
    var car: Float = 0.0f,
    var posuda: Float = 0.0f,
    var travel: Float = 0.0f,
    var gifts: Float = 0.0f,
    var snacks: Float = 0.0f,
    var medicine: Float = 0.0f,
    var cosmetics: Float = 0.0f,
    var higien: Float = 0.0f,
    var other: Float = 0.0f,
    var domPotrebi: Float = 0.0f,
    var preparati: Float = 0.0f,
    var clean: Float = 0.0f,
    var wash: Float = 0.0f,
    var machove: Float = 0.0f,
    var furniture: Float = 0.0f,
    var tehnika: Float = 0.0f,
    var education: Float = 0.0f,
    var entertainment: Float = 0.0f,
    var subscriptions: Float = 0.0f,
    var tattoo: Float = 0.0f,
    var toys: Float = 0.0f,
    @PrimaryKey
    val id: String
)
