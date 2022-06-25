package com.example.sledipari.utility.extensions

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.sledipari.R
import com.example.sledipari.data.models.Month
import com.example.sledipari.ui.*
import com.example.sledipari.utility.formatDate

fun Month.toList(): List<Pair<Pair<Float, String>, Color>> {

    val list: List<Pair<Pair<Float, String>, Color>>
    val mutableList = mutableListOf<Pair<Pair<Float, String>, Color>>()

    for (member in this::class.members) {

        val value = getMonthValueAndColor(this, member.name) ?: continue
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

fun Month.totalSum(): Float {

    return this.clothes +
            this.workout +
            this.remont +
            this.food +
            this.smetki +
            this.transport +
            this.posuda +
            this.travel +
            this.gifts +
            this.snacks +
            this.medicine +
            this.cosmetics +
            this.domPotrebi +
            this.preparati +
            this.machove +
            this.furniture +
            this.tehnika +
            this.education +
            this.entertainment +
            this.subscriptions +
            this.tattoo +
            this.toys
}

fun getMonthValueAndColor(month: Month, name: String): Pair<Pair<Float, String>, Color>? {

    return when (name) {
        "clothes" -> Pair(Pair(month.clothes, name), clothes)
        "workout" -> Pair(Pair(month.workout, name), workout)
        "remont" -> Pair(Pair(month.remont, name), remont)
        "posuda" -> Pair(Pair(month.posuda, name), posuda)
        "travel" -> Pair(Pair(month.travel, name), travel)
        "gifts" -> Pair(Pair(month.gifts, name), gifts)
        "snacks" -> Pair(Pair(month.snacks, name), snacks)
        "medicine" -> Pair(Pair(month.medicine, name), medicine)
        "domPotrebi" -> Pair(Pair(month.domPotrebi, name), domPotrebi)
        "machove" -> Pair(Pair(month.machove, name), machove)
        "furniture" -> Pair(Pair(month.furniture, name), furniture)
        "tehnika" -> Pair(Pair(month.tehnika, name), tehnika)
        "education" -> Pair(Pair(month.education, name), education)
        "entertainment" -> Pair(Pair(month.entertainment, name), entertainment)
        "subscriptions" -> Pair(Pair(month.subscriptions, name), subscriptions)
        "tattoo" -> Pair(Pair(month.tattoo, name), tattoo)
        "toys" -> Pair(Pair(month.toys, name), toys)
        "food" -> Pair(Pair(month.food, name), food)
        "smetki" -> Pair(Pair(month.smetki, name), smetki)
        "transport" -> Pair(Pair(month.transport, name), transport)
        "cosmetics" -> Pair(Pair(month.cosmetics, name), cosmetics)
        "preparati" -> Pair(Pair(month.preparati, name), preparati)
        else -> null
    }
}

fun Month.isCurrent(): Boolean {

    return this.id == System.currentTimeMillis().formatDate("yyyy-MM")
}

fun Month.getCurrentCategoryValue(context: Context, category: String): Float {
    return when (category) {
        context.getString(R.string.food) -> this.food
        context.getString(R.string.smetki) -> this.smetki
        context.getString(R.string.transport) -> this.transport
        context.getString(R.string.cosmetics) -> this.cosmetics
        context.getString(R.string.preparati) -> this.preparati
        else -> 99999f
    }
}