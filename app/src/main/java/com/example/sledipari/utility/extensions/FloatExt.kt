package com.example.sledipari.utility.extensions

fun Float.toPercent(totalSum: Float): Float {

    return (100 * this) / totalSum
}

fun Float.formatPrice(): String {
    return String.format("%.2f", this)
}