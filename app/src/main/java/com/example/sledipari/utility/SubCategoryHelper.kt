package com.example.sledipari.utility

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.sledipari.R
import com.example.sledipari.api.models.categories.*
import com.example.sledipari.data.models.Month
import com.example.sledipari.ui.*
import com.example.sledipari.ui.main.SpItem

fun String.getTitle(context: Context): String {

    return when(this) {
        "food" -> context.getString(R.string.food)
        "smetki" -> context.getString(R.string.smetki)
        "transport" -> context.getString(R.string.transport)
        "cosmetics" -> context.getString(R.string.cosmetics)
        "preparati" -> context.getString(R.string.preparati)
        "frizior" -> context.getString(R.string.frizior)
        else -> ""
    }
}

fun foodToList(month: Month): List<SpItem> {

    val list: List<SpItem>
    val mutableList = mutableListOf<SpItem>()

    for (member in Food::class.members) {

        val value = getFoodValueAndColor(month, member.name) ?: continue
        if (value.price != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.price
    })

    list = mutableList.reversed()
    return list
}

fun smetkiToList(month: Month): List<SpItem> {

    val list: List<SpItem>
    val mutableList = mutableListOf<SpItem>()

    for (member in Smetki::class.members) {

        val value = getSmetkiValueAndColor(month, member.name) ?: continue
        if (value.price != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.price
    })

    list = mutableList.reversed()
    return list
}

fun friziorToList(month: Month): List<SpItem> {

    val list: List<SpItem>
    val mutableList = mutableListOf<SpItem>()

    for (member in Frizior::class.members) {

        val value = getFriziorValueAndColor(month, member.name) ?: continue
        if (value.price != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.price
    })

    list = mutableList.reversed()
    return list
}

fun transportToList(month: Month): List<SpItem> {

    val list: List<SpItem>
    val mutableList = mutableListOf<SpItem>()

    for (member in Transport::class.members) {

        val value = getTransportValueAndColor(month, member.name) ?: continue
        if (value.price != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.price
    })

    list = mutableList.reversed()
    return list
}

fun preparatiToList(month: Month): List<SpItem> {

    val list: List<SpItem>
    val mutableList = mutableListOf<SpItem>()

    for (member in Preparati::class.members) {

        val value = getPreparatiValueAndColor(month, member.name) ?: continue
        if (value.price != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.price
    })

    list = mutableList.reversed()
    return list
}

fun cosmeticsToList(month: Month): List<SpItem> {

    val list: List<SpItem>
    val mutableList = mutableListOf<SpItem>()

    for (member in Cosmetics::class.members) {

        val value = getCosmeticsValueAndColor(month, member.name) ?: continue
        if (value.price != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.price
    })

    list = mutableList.reversed()
    return list
}

fun getFoodValueAndColor(month: Month, name: String): SpItem? {

    return when (name) {
        "home" -> SpItem(month.home, name, home)
        "restaurant" -> SpItem(month.restaurant, name, restaurant)
        else -> null
    }
}

fun getSmetkiValueAndColor(month: Month, name: String): SpItem? {

    return when (name) {
        "tok" -> SpItem(month.tok, name, tok)
        "voda" -> SpItem(month.voda, name, voda)
        "toplo" -> SpItem(month.toplo, name, toplo)
        "internet" -> SpItem(month.internet, name, internet)
        "vhod" -> SpItem(month.vhod, name, vhod)
        "telefon" -> SpItem(month.telefon, name, telefon)
        else -> null
    }
}

fun getFriziorValueAndColor(month: Month, name: String): SpItem? {

    return when (name) {
        "friziorSub" -> SpItem(month.friziorSub, name, friziorSub)
        "cosmetic" -> SpItem(month.cosmetic, name, cosmetic)
        "manikior" -> SpItem(month.manikior, name, manikior)
        else -> null
    }
}

fun getTransportValueAndColor(month: Month, name: String): SpItem? {

    return when (name) {
        "public" -> SpItem(month.publicT, name, publicT)
        "taxi" -> SpItem(month.taxi, name, taxi)
        "car" -> SpItem(month.car, name, car)
        else -> null
    }
}

fun getCosmeticsValueAndColor(month: Month, name: String): SpItem? {

    return when (name) {
        "higien" -> SpItem(month.higien, name, higien)
        "other" -> SpItem(month.other, name, other)
        else -> null
    }
}

fun getPreparatiValueAndColor(month: Month, name: String): SpItem? {

    return when (name) {
        "clean" -> SpItem(month.clean, name, clean)
        "wash" -> SpItem(month.wash, name, wash)
        else -> null
    }
}