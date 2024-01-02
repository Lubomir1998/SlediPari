package com.example.sledipari.data.models

import androidx.room.Entity
import com.example.sledipari.api.models.HubDTO

@Entity
data class Hub(
    val name: String,
    val users: List<String>,
    val owner: String,
    val id: String
)
