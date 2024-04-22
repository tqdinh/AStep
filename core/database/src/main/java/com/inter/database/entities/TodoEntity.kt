package com.inter.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "TODO", primaryKeys = ["userId"])
data class TodoEntity(

    @ColumnInfo val userId:Int,
    @ColumnInfo val id:Int,
    @ColumnInfo val title:String,
    @ColumnInfo val completed:Boolean
)