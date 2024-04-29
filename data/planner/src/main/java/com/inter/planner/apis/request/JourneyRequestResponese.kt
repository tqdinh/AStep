package com.inter.planner.apis.request

import com.google.gson.annotations.SerializedName

data class JourneyRequest
    (
    val id: String,
    val created_time: Long,
    val updated_time: Long,
    val title: String,
    val desc: String,
)
data class JourneyResponse
    (
    @SerializedName("name")
    val id: String,
)