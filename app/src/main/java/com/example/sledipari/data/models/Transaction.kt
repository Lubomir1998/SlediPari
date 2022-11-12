package com.example.sledipari.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Transaction(
    val price: Float,
    val title: String,
    val red: Float,
    val green: Float,
    val blue: Float,
    val undo: Boolean,
    @PrimaryKey
    val timestamp: Long
)