package com.example.sledipari.utility

import com.example.sledipari.utility.Constants.BASE_URL
import com.example.sledipari.utility.Constants.BASE_URL_LOCALHOST
import com.example.sledipari.utility.Constants.USE_LOCALHOST

fun baseUrl(isLocalHost: Boolean = USE_LOCALHOST): String {
    return if (isLocalHost) {
        BASE_URL_LOCALHOST
    } else {
        BASE_URL
    }
}