package com.inter.explore.entities

import com.google.gson.annotations.SerializedName
import com.inter.explore.api.DTO

data class TodoDTO
    (
    @SerializedName("userId") val userId: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("completed") val completed: Boolean
) : DTO<Todo> {
    override fun asDomain():Todo {
         return Todo(userId,id,title,completed)
    }

}