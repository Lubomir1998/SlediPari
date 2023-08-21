package com.example.sledipari.ui

import androidx.compose.ui.graphics.Color

val clothes = Color(0xFFF93AFC)
val workout = Color(0xFFC1FE1A)
val remont = Color(0xFFD54B13)
val food = Color(0xFFFFA41A)
val smetki = Color(0xFF1AA6FE)
val transport = Color(0xFF34F111)
val posuda = Color(0xFFFA7381)
val travel = Color(0xFF1AFFDE)
val gifts = Color(0xFFC51AFF)
val snacks = Color(0xFFF11129)
val medicine = Color(0xFF88CDFF)
val cosmetics = Color(0xFFFFF81A)
val domPotrebi = Color(0xFF94FA83)
val preparati = Color(0xFFF2219C)
val machove = Color(0xFF156EAE)
val furniture = Color(0xFF884743)
val tehnika = Color(0xFFABC1C8)
val education = Color(0xFFF6E36A)
val entertainment = Color(0xFFCAFB03)
val subscriptions = Color(0xFF94FEDF)
val tattoo = Color(0xFFD594FE)
val toys = Color(0xFFEAFE94)
val frizior = Color(0xFF573DFF)

//Food
val home = Color(0xFF1BDBFF)
val restaurant = Color(0xFFFB8603)

//Smetki
val tok = Color(0xFF56FF1B)
val voda = Color(0xFF1BDBFF)
val toplo = Color(0xFFFF3A1B)
val internet = Color(0xFF08FCCA)
val telefon = Color(0xFFFC08F7)
val vhod = Color(0xFFFCF708)

//Transport
val publicT = Color(0xFF08FCEB)
val taxi = Color(0xFFFC08F0)
val car = Color(0xFFFCF708)

//Cosmetics
val higien = Color(0xFF08FC37)
val other = Color(0xFFCE65FE)

//Preparati
val clean = Color(0xFF08FC37)
val wash = Color(0xFFCE65FE)

// Frizior
val friziorSub = Color(0xFF573DFF)
val cosmetic = Color(0xFF4CF716)
val manikior = Color(0xFFF71A3C)

// Other
val divider = Color(0xFFC6C6C8)

fun String.getRGB(): Triple<Float, Float, Float> {
    return when (this) {
        "clothes" -> Triple(clothes.red, clothes.green, clothes.blue)
        "workout" -> Triple(workout.red, workout.green, workout.blue)
        "remont" -> Triple(remont.red, remont.green, remont.blue)
        "posuda" -> Triple(posuda.red, posuda.green, posuda.blue)
        "travel" -> Triple(travel.red, travel.green, travel.blue)
        "gifts" -> Triple(gifts.red, gifts.green, gifts.blue)
        "snacks" -> Triple(snacks.red, snacks.green, snacks.blue)
        "medicine" -> Triple(medicine.red, medicine.green, medicine.blue)
        "domPotrebi" -> Triple(domPotrebi.red, domPotrebi.green, domPotrebi.blue)
        "machove" -> Triple(machove.red, machove.green, machove.blue)
        "furniture" -> Triple(furniture.red, furniture.green, furniture.blue)
        "tehnika" -> Triple(tehnika.red, tehnika.green, tehnika.blue)
        "education" -> Triple(education.red, education.green, education.blue)
        "entertainment" -> Triple(entertainment.red, entertainment.green, entertainment.blue)
        "subscriptions" -> Triple(subscriptions.red, subscriptions.green, subscriptions.blue)
        "tattoo" -> Triple(tattoo.red, tattoo.green, tattoo.blue)
        "toys" -> Triple(toys.red, toys.green, toys.blue)
        "tok", "voda", "toplo", "internet", "telefon", "vhod" -> Triple(smetki.red, smetki.green, smetki.blue)
        "home", "restaurant" -> Triple(food.red, food.green, food.blue)
        "higien", "other" -> Triple(cosmetics.red, cosmetics.green, cosmetics.blue)
        "clean", "wash" -> Triple(preparati.red, preparati.green, preparati.blue)
        "friziorSub", "cosmetic", "manikior" -> Triple(frizior.red, frizior.green, frizior.blue)
        "public", "taxi", "car" -> Triple(transport.red, transport.green, transport.blue)
        else -> Triple(0f, 0f, 0f)
    }
}