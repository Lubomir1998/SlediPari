package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val email: String,
    val familyName: String,
    val givenName: String,
    val locale: String,
    val name: String,
    val nickname: String,
    val picture: String,
    val id: String
)