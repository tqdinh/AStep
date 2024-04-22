package com.inter.database.entities

import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "LocalImageTable", primaryKeys = ["id", "place_id"])
data class LocalImageTmp(

    val id: String = UUID.randomUUID().toString(),

    val place_id: String,

    val timestamp: Long = System.currentTimeMillis(),

    val path: String = "",

    val title: String = "",

    val desc: String = "",

    var lat: Double = 0.0,

    var lon: Double = 0.0,

    )