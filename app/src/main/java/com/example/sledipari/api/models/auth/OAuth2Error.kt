package com.example.sledipari.api.models.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.lang.Error

@Serializable
data class OAuth2Error(
    val error: String,
    @SerialName("error_description")
    val description: String? = null
) {

    fun getException(): OAuth2Exception? {

        return when(this.error) {

            "invalid_request" -> InvalidRequestException(this.description)
            "invalid_client" -> InvalidClientException(this.description)
            "invalid_grant" -> InvalidGrantException(this.description)
            "unauthorized_client" -> UnauthorizedClientException(this.description)
            "unsupported_grant_type" -> UnsupportedGrantTypeException(this.description)
            "invalid_scope" -> InvalidScopeException(this.description)
            else -> null
        }
    }
}
