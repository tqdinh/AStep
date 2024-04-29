package com.inter.planner.apis.request

import com.google.gson.annotations.SerializedName
import com.inter.entity.planner.ImageEntity
import java.util.UUID

data class ImageRequest(
    val id: String,
    val place_id: String,
    val path: String = "",
    val url: String = "",
)

data class ImageResponse
    (
    @SerializedName("name")
    val id: String,
)