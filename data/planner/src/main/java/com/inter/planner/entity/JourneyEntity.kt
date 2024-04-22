package com.inter.planner.entity


data class JourneyEntity(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val title: String = "",
    val desc: String = "",
    var listPlaces: List<PlaceEntity> = emptyList()
)