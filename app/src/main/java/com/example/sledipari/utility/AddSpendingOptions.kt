package com.example.sledipari.utility

import android.content.Context
import com.example.sledipari.R

fun addingOptions(context: Context): HashMap<String, String> {
    return linkedMapOf(
        context.getString(R.string.food_home) to "home",
        context.getString(R.string.food_res) to "restaurant",
        context.getString(R.string.smetki_tok) to "tok",
        context.getString(R.string.smetki_voda) to "voda",
        context.getString(R.string.smetki_toplo) to "toplo",
        context.getString(R.string.smetki_internet) to "internet",
        context.getString(R.string.smetki_telefon) to "telefon",
        context.getString(R.string.smetki_vhod) to "vhod",
        context.getString(R.string.transport_public) to "public",
        context.getString(R.string.transport_car) to "car",
        context.getString(R.string.transport_taxi) to "taxi",
        context.getString(R.string.clothes) to "clothes",
        context.getString(R.string.workout) to "workout",
        context.getString(R.string.frizior) to "friziorSub",
        context.getString(R.string.cosmetic) to "cosmetic",
        context.getString(R.string.manikior) to "manikior",
        context.getString(R.string.remont) to "remont",
        context.getString(R.string.posuda) to "posuda",
        context.getString(R.string.travel) to "travel",
        context.getString(R.string.gifts) to "gifts",
        context.getString(R.string.snacks) to "snacks",
        context.getString(R.string.health) to "medicine",
        context.getString(R.string.domPot) to "domPotrebi",
        context.getString(R.string.tehnika) to "tehnika",
        context.getString(R.string.machove) to "machove",
        context.getString(R.string.furniture) to "furniture",
        context.getString(R.string.education) to "education",
        context.getString(R.string.entertainment) to "entertainment",
        context.getString(R.string.tattoo) to "tattoo",
        context.getString(R.string.toys) to "toys",
        context.getString(R.string.cosmetics_higien) to "higien",
        context.getString(R.string.cosmetics_other) to "other",
        context.getString(R.string.preparati_clean) to "clean",
        context.getString(R.string.preparati_wash) to "wash",
        context.getString(R.string.subscribtions) to "subscribtions"
    )
}