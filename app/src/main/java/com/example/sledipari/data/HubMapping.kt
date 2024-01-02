package com.example.sledipari.data

import com.example.sledipari.api.models.HubDTO
import com.example.sledipari.data.models.Hub

fun HubDTO.toHub(): Hub {

    return Hub(this.name, this.users, this.owner, this.id)
}