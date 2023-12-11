package com.example.sledipari.api.models.auth

class InvalidClientException(message: String? = null):
    OAuth2Exception(message ?: "Unknown client, no client authentication included, or unsupported authentication method")