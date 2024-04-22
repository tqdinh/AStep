package com.inter.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "journey_table", primaryKeys = ["journey_id"])
data class LocalJourneyEntity(
    @ColumnInfo("journey_id") val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val title: String = "",
    val desc: String = "",
)