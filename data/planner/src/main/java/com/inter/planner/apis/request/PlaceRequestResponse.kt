package com.inter.planner.apis.request

import com.google.gson.annotations.SerializedName
import com.inter.entity.planner.ImageEntity
import java.util.UUID

data class PlaceRequest(
    val id: String = UUID.randomUUID().toString(),
    val journey_id: String,
    val created_time: Long,
    val updated_time: Long,
    val title: String = "",
    val desc: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
)

data class PlaceResponse
    (
    @SerializedName("name")
    val id: String,
)