package com.inter.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "place_table", primaryKeys = ["place_id"])
data class LocalPlace(
    @ColumnInfo("place_id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo("ref_journey_id") val ref_journey_id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val title: String = "",
    val desc: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
)
