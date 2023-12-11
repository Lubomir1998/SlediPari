package com.example.sledipari.api.models.auth

class UnsupportedGrantTypeException(message: String? = null):
    OAuth2Exception(message ?: "The authorization grant type is not supported by the authorization server")