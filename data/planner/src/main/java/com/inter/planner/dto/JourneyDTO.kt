package com.inter.planner.dto

import com.inter.entity.planner.PlaceEntity

data class JourneyDTO(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val title: String = "",
    val desc: String = "",
    var listPlaces: List<PlaceEntity> = emptyList()
)
