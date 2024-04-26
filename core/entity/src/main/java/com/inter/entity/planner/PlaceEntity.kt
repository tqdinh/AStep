package com.inter.entity.planner


import java.util.UUID


data class PlaceEntity(
    val id: String = UUID.randomUUID().toString(),
    val ref_journey_id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String = "",
    val desc: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var listImage: List<ImageEntity> = emptyList()
)
