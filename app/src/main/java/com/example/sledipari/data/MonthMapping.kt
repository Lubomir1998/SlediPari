package com.example.sledipari.data

import com.example.sledipari.api.models.MonthDTO
import com.example.sledipari.data.models.Month

fun MonthDTO.toMonth(): Month {
    return Month(
        clothes = this.clothes,
        workout = this.workout,
        remont = this.remont,
        food = this.food.home + this.food.restaurant,
        home = this.food.home,
        restaurant = this.food.restaurant,
        smetki = this.smetki.tok + this.smetki.voda + this.smetki.toplo + this.smetki.internet + this.smetki.telefon + this.smetki.vhod,
        tok = this.smetki.tok,
        voda = this.smetki.voda,
        toplo = this.smetki.toplo,
        internet = this.smetki.internet,
        vhod = this.smetki.vhod,
        telefon = this.smetki.telefon,
        transport = this.transport.public + this.transport.taxi + this.transport.car,
        publicT = this.transport.public,
        taxi = this.transport.taxi,
        car = this.transport.car,
        posuda = this.posuda,
        travel = this.travel,
        gifts = this.gifts,
        snacks = this.snacks,
        medicine = this.medicine,
        cosmetics = this.cosmetics.higien + this.cosmetics.other,
        higien = this.cosmetics.higien,
        other = this.cosmetics.other,
        domPotrebi = this.domPotrebi,
        preparati = this.preparati.clean + this.preparati.wash,
        clean = this.preparati.clean,
        wash = this.preparati.wash,
        machove = this.machove,
        furniture = this.furniture,
        tehnika = this.tehnika,
        education = this.education,
        entertainment = this.entertainment,
        subscriptions = this.subscriptions,
        tattoo = this.tattoo,
        toys = this.toys,
        id = this.id
    )
}