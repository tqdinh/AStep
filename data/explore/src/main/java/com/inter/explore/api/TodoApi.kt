package com.inter.explore.api

import com.inter.explore.entities.TodoDTO
import retrofit2.Response
import retrofit2.http.GET

interface TodoApi {
    @GET("https://jsonplaceholder.typicode.com/todos")
    suspend fun getTodo(): Response<List<TodoDTO>>
}