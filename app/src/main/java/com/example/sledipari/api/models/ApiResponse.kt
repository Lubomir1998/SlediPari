package com.example.sledipari.api.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<Result>(
    var result: Result? = null,
    var errors: String? = null,
    val isSuccess: Boolean
) {

    fun parse(): Result {

        return result ?: throw Throwable(errors)
    }
}
