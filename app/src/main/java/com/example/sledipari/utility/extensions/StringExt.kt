package com.example.sledipari.utility.extensions

import android.content.Context
import com.example.sledipari.R

fun String.toLocalizable(context: Context): String {
    return when (this) {
        "clothes" -> context.getString(R.string.clothes)
        "workout" -> context.getString(R.string.workout)
        "remont" -> context.getString(R.string.remont)
        "posuda" -> context.getString(R.string.posuda)
        "travel" -> context.getString(R.string.travel)
        "gifts" -> context.getString(R.string.gifts)
        "snacks" -> context.getString(R.string.snacks)
        "medicine" -> context.getString(R.string.health)
        "domPotrebi" -> context.getString(R.string.domPot)
        "machove" -> context.getString(R.string.machove)
        "furniture" -> context.getString(R.string.furniture)
        "tehnika" -> context.getString(R.string.tehnika)
        "education" -> context.getString(R.string.education)
        "entertainment" -> context.getString(R.string.entertainment)
        "subscriptions" -> context.getString(R.string.subscribtions)
        "tattoo" -> context.getString(R.string.tattoo)
        "toys" -> context.getString(R.string.toys)
        "food" -> context.getString(R.string.food)
        "smetki" -> context.getString(R.string.smetki)
        "transport" -> context.getString(R.string.transport)
        "cosmetics" -> context.getString(R.string.cosmetics)
        "preparati" -> context.getString(R.string.preparati)
        "home" -> context.getString(R.string.home)
        "restaurant" -> context.getString(R.string.restaurant)
        "tok" -> context.getString(R.string.tok)
        "voda" -> context.getString(R.string.voda)
        "toplo" -> context.getString(R.string.toplo)
        "internet" -> context.getString(R.string.internet)
        "vhod" -> context.getString(R.string.vhod)
        "telefon" -> context.getString(R.string.telefon)
        "public" -> context.getString(R.string.publicT)
        "taxi" -> context.getString(R.string.taxi)
        "car" -> context.getString(R.string.car)
        "higien" -> context.getString(R.string.higien)
        "other" -> context.getString(R.string.other)
        "clean" -> context.getString(R.string.clean)
        "wash" -> context.getString(R.string.wash)
        "frizior" -> context.getString(R.string.frizior)
        "friziorSub" -> context.getString(R.string.frizior)
        "cosmetic" -> context.getString(R.string.cosmetic)
        "manikior" -> context.getString(R.string.manikior)
        else -> ""
    }
}

fun String.flagEmoji(): String {

    val firstLetter = Character.codePointAt(this.toCharArray(), 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(this.toCharArray(), 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}