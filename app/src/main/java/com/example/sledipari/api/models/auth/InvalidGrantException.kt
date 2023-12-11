package com.example.sledipari.api.models.auth

class InvalidGrantException(message: String? = null):
    OAuth2Exception(message ?: "The provided authorization grant or refresh token is invalid")