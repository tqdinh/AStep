package com.inter.planner.entity


import java.util.UUID


data class ImageEntity(
    val id: String = UUID.randomUUID().toString(),
    val ref_place_id: String,
    val path: String = "",
    val url: String = "",
)