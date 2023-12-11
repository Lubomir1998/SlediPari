package com.example.sledipari.api.models.auth

class InvalidRequestException(message: String? = null):
        OAuth2Exception(message ?: "The request is missing a required parameter")