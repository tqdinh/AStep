package com.inter.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "image_table", primaryKeys = ["img_id"])
data class LocalImage(
    @ColumnInfo("img_id") val id: String = UUID.randomUUID().toString(),
//    val createTime:Long,
//    val updateTime:Long,
    val ref_place_id: String,
    val path: String = "",
    val url: String = "",
)