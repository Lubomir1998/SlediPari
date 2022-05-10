package com.example.sledipari.utility

import android.annotation.SuppressLint
import com.example.sledipari.data.models.Month
import java.text.SimpleDateFormat
import java.util.*

fun Long.formatDate(pattern: String): String {

    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}

@SuppressLint("SimpleDateFormat")
fun String.toReadableDate(): String {

    val date = SimpleDateFormat("MM-yyyy").parse(this)
    return date.time.formatDate("MMM yyy")
}