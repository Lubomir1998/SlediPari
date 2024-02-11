package com.example.sledipari.utility.extensions

import android.content.Context
import com.example.sledipari.R
import com.example.sledipari.data.models.Month
import com.example.sledipari.ui.*
import com.example.sledipari.ui.main.months.SpItem
import com.example.sledipari.utility.formatDate

fun Month.toList(): List<SpItem> {

    val list: List<SpItem>
    val mutableList = mutableListOf<SpItem>()

    for (member in this::class.members) {

        val value = getMonthValueAndColor(this, member.name) ?: continue
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

fun getMonthValueAndColor(month: Month, name: String): SpItem? {

    return when (name) {
        "clothes" -> SpItem(month.clothes, name, clothes)
        "workout" -> SpItem(month.workout, name, workout)
        "remont" -> SpItem(month.remont, name, remont)
        "posuda" -> SpItem(month.posuda, name, posuda)
        "travel" -> SpItem(month.travel, name, travel)
        "gifts" -> SpItem(month.gifts, name, gifts)
        "snacks" -> SpItem(month.snacks, name, snacks)
        "medicine" -> SpItem(month.medicine, name, medicine)
        "domPotrebi" -> SpItem(month.domPotrebi, name, domPotrebi)
        "machove" -> SpItem(month.machove, name, machove)
        "furniture" -> SpItem(month.furniture, name, furniture)
        "tehnika" -> SpItem(month.tehnika, name, tehnika)
        "education" -> SpItem(month.education, name, education)
        "entertainment" -> SpItem(month.entertainment, name, entertainment)
        "subscriptions" -> SpItem(month.subscriptions, name, subscriptions)
        "tattoo" -> SpItem(month.tattoo, name, tattoo)
        "toys" -> SpItem(month.toys, name, toys)
        "food" -> SpItem(month.food, name, food)
        "smetki" -> SpItem(month.smetki, name, smetki)
        "transport" -> SpItem(month.transport, name, transport)
        "cosmetics" -> SpItem(month.cosmetics, name, cosmetics)
        "preparati" -> SpItem(month.preparati, name, preparati)
        "frizior" -> SpItem(month.frizior, name, frizior)
        else -> null
    }
}

fun getMonthValueAndColor2(month: Month, name: String): SpItem? {

    return when (name) {
        "clothes" -> SpItem(month.clothes, name, clothes)
        "workout" -> SpItem(month.workout, name, workout)
        "remont" -> SpItem(month.remont, name, remont)
        "posuda" -> SpItem(month.posuda, name, posuda)
        "travel" -> SpItem(month.travel, name, travel)
        "gifts" -> SpItem(month.gifts, name, gifts)
        "snacks" -> SpItem(month.snacks, name, snacks)
        "medicine" -> SpItem(month.medicine, name, medicine)
        "domPotrebi" -> SpItem(month.domPotrebi, name, domPotrebi)
        "machove" -> SpItem(month.machove, name, machove)
        "furniture" -> SpItem(month.furniture, name, furniture)
        "tehnika" -> SpItem(month.tehnika, name, tehnika)
        "education" -> SpItem(month.education, name, education)
        "entertainment" -> SpItem(month.entertainment, name, entertainment)
        "subscriptions" -> SpItem(month.subscriptions, name, subscriptions)
        "tattoo" -> SpItem(month.tattoo, name, tattoo)
        "toys" -> SpItem(month.toys, name, toys)
        "home" -> SpItem(month.home, name, food)
        "restaurant" -> SpItem(month.restaurant, name, food)
        "tok" -> SpItem(month.tok, name, smetki)
        "voda" -> SpItem(month.voda, name, smetki)
        "toplo" -> SpItem(month.toplo, name, smetki)
        "internet" -> SpItem(month.internet, name, smetki)
        "vhod" -> SpItem(month.vhod, name, smetki)
        "telefon" -> SpItem(month.telefon, name, smetki)
        "public" -> SpItem(month.publicT, name, transport)
        "taxi" -> SpItem(month.taxi, name, transport)
        "car" -> SpItem(month.car, name, transport)
        "higien" -> SpItem(month.higien, name, cosmetics)
        "other" -> SpItem(month.other, name, cosmetics)
        "wash" -> SpItem(month.wash, name, preparati)
        "clean" -> SpItem(month.clean, name, preparati)
        "friziorSub" -> SpItem(month.friziorSub, name, frizior)
        "cosmetic" -> SpItem(month.cosmetic, name, frizior)
        "manikior" -> SpItem(month.manikior, name, frizior)
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