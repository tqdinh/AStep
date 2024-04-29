package com.inter.entity.planner


data class JourneyEntity(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val title: String = "",
    val desc: String = "",
    var listPlaces: MutableList<PlaceEntity> = mutableListOf()
)