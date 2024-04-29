package com.inter.planner.dto

import com.inter.entity.planner.PlaceEntity

data class JourneyDTO(
    val id: String = "",
    val timestamp: Long,
    val title: String = "",
    val desc: String = "",
    var listPlaces: MutableList<PlaceEntity> = mutableListOf()
)
