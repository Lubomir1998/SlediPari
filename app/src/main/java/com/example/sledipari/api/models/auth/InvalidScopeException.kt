package com.example.sledipari.api.models.auth

class InvalidScopeException(message: String? = null):
        OAuth2Exception(message ?: "The requested scope is invalid")