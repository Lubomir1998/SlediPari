package com.example.sledipari.api.models.auth

class UnauthorizedClientException(message: String? = null):
    OAuth2Exception(message ?: "The authenticated client is not authorized to use this authorization grant type")