package com.example.sledipari.utility.extensions

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.sledipari.R
import com.example.sledipari.data.models.Month
import com.example.sledipari.ui.*
import com.example.sledipari.ui.main.SpItem
import com.example.sledipari.utility.formatDate

fun Month.toList(): List<SpItem> {

    val list: List<SpItem>
    val mutableList = mutableListOf<SpItem>()

    for (member in this::class.members) {

        val value = getMonthValueAndColor(this, member.name) ?: continue
        if (value.first.first != 0f) {
            mutableList.add(Pair(value, true))
        }
    }

    mutableList.sortWith(compareBy {
        it.first.first.first
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
            this.frizior +
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
        "frizior" -> Pair(Pair(month.frizior, name), frizior)
        else -> null
    }
}

fun getMonthValueAndColor2(month: Month, name: String): Pair<Pair<Float, String>, Color>? {

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
        "home" -> Pair(Pair(month.home, name), food)
        "restaurant" -> Pair(Pair(month.restaurant, name), food)
        "tok" -> Pair(Pair(month.tok, name), smetki)
        "voda" -> Pair(Pair(month.voda, name), smetki)
        "toplo" -> Pair(Pair(month.toplo, name), smetki)
        "internet" -> Pair(Pair(month.internet, name), smetki)
        "vhod" -> Pair(Pair(month.vhod, name), smetki)
        "telefon" -> Pair(Pair(month.telefon, name), smetki)
        "public" -> Pair(Pair(month.publicT, name), transport)
        "taxi" -> Pair(Pair(month.taxi, name), transport)
        "car" -> Pair(Pair(month.car, name), transport)
        "higien" -> Pair(Pair(month.higien, name), cosmetics)
        "other" -> Pair(Pair(month.other, name), cosmetics)
        "wash" -> Pair(Pair(month.wash, name), preparati)
        "clean" -> Pair(Pair(month.clean, name), preparati)
        "friziorSub" -> Pair(Pair(month.friziorSub, name), frizior)
        "cosmetic" -> Pair(Pair(month.cosmetic, name), frizior)
        "manikior" -> Pair(Pair(month.manikior, name), frizior)
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
        context.getString(R.string.frizior) -> this.frizior
        else -> 99999f
    }
}