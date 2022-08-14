package com.example.sledipari.utility

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.data.models.Frizior
import com.example.sledipari.R
import com.example.sledipari.api.models.*
import com.example.sledipari.data.models.Month
import com.example.sledipari.ui.*

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

fun foodToList(month: Month): List<Pair<Pair<Float, String>, Color>> {

    val list: List<Pair<Pair<Float, String>, Color>>
    val mutableList = mutableListOf<Pair<Pair<Float, String>, Color>>()

    for (member in Food::class.members) {

        val value = getFoodValueAndColor(month, member.name) ?: continue
        if (value.first.first != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.first.first
    })

    list = mutableList.reversed()
    return list
}

fun smetkiToList(month: Month): List<Pair<Pair<Float, String>, Color>> {

    val list: List<Pair<Pair<Float, String>, Color>>
    val mutableList = mutableListOf<Pair<Pair<Float, String>, Color>>()

    for (member in Smetki::class.members) {

        val value = getSmetkiValueAndColor(month, member.name) ?: continue
        if (value.first.first != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.first.first
    })

    list = mutableList.reversed()
    return list
}

fun friziorToList(month: Month): List<Pair<Pair<Float, String>, Color>> {

    val list: List<Pair<Pair<Float, String>, Color>>
    val mutableList = mutableListOf<Pair<Pair<Float, String>, Color>>()

    for (member in Frizior::class.members) {

        val value = getFriziorValueAndColor(month, member.name) ?: continue
        if (value.first.first != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.first.first
    })

    list = mutableList.reversed()
    return list
}

fun transportToList(month: Month): List<Pair<Pair<Float, String>, Color>> {

    val list: List<Pair<Pair<Float, String>, Color>>
    val mutableList = mutableListOf<Pair<Pair<Float, String>, Color>>()

    for (member in Transport::class.members) {

        val value = getTransportValueAndColor(month, member.name) ?: continue
        if (value.first.first != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.first.first
    })

    list = mutableList.reversed()
    return list
}

fun preparatiToList(month: Month): List<Pair<Pair<Float, String>, Color>> {

    val list: List<Pair<Pair<Float, String>, Color>>
    val mutableList = mutableListOf<Pair<Pair<Float, String>, Color>>()

    for (member in Preparati::class.members) {

        val value = getPreparatiValueAndColor(month, member.name) ?: continue
        if (value.first.first != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.first.first
    })

    list = mutableList.reversed()
    return list
}

fun cosmeticsToList(month: Month): List<Pair<Pair<Float, String>, Color>> {

    val list: List<Pair<Pair<Float, String>, Color>>
    val mutableList = mutableListOf<Pair<Pair<Float, String>, Color>>()

    for (member in Cosmetics::class.members) {

        val value = getCosmeticsValueAndColor(month, member.name) ?: continue
        if (value.first.first != 0f) {
            mutableList.add(value)
        }
    }

    mutableList.sortWith(compareBy {
        it.first.first
    })

    list = mutableList.reversed()
    return list
}

fun getFoodValueAndColor(month: Month, name: String): Pair<Pair<Float, String>, Color>? {

    return when (name) {
        "home" -> Pair(Pair(month.home, name), home)
        "restaurant" -> Pair(Pair(month.restaurant, name), restaurant)
        else -> null
    }
}

fun getSmetkiValueAndColor(month: Month, name: String): Pair<Pair<Float, String>, Color>? {

    return when (name) {
        "tok" -> Pair(Pair(month.tok, name), tok)
        "voda" -> Pair(Pair(month.voda, name), voda)
        "toplo" -> Pair(Pair(month.toplo, name), toplo)
        "internet" -> Pair(Pair(month.internet, name), internet)
        "vhod" -> Pair(Pair(month.vhod, name), vhod)
        "telefon" -> Pair(Pair(month.telefon, name), telefon)
        else -> null
    }
}

fun getFriziorValueAndColor(month: Month, name: String): Pair<Pair<Float, String>, Color>? {

    return when (name) {
        "friziorSub" -> Pair(Pair(month.friziorSub, name), friziorSub)
        "cosmetic" -> Pair(Pair(month.cosmetic, name), cosmetic)
        "manikior" -> Pair(Pair(month.manikior, name), manikior)
        else -> null
    }
}

fun getTransportValueAndColor(month: Month, name: String): Pair<Pair<Float, String>, Color>? {

    return when (name) {
        "public" -> Pair(Pair(month.publicT, name), publicT)
        "taxi" -> Pair(Pair(month.taxi, name), taxi)
        "car" -> Pair(Pair(month.car, name), car)
        else -> null
    }
}

fun getCosmeticsValueAndColor(month: Month, name: String): Pair<Pair<Float, String>, Color>? {

    return when (name) {
        "higien" -> Pair(Pair(month.higien, name), higien)
        "other" -> Pair(Pair(month.other, name), other)
        else -> null
    }
}

fun getPreparatiValueAndColor(month: Month, name: String): Pair<Pair<Float, String>, Color>? {

    return when (name) {
        "clean" -> Pair(Pair(month.clean, name), clean)
        "wash" -> Pair(Pair(month.wash, name), wash)
        else -> null
    }
}