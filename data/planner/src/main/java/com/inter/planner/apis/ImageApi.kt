package com.inter.network.apis

import com.inter.planner.apis.request.ImageRequest
import com.inter.planner.apis.request.JourneyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ImageApi {

    @POST("https://onestep-4004e-default-rtdb.asia-southeast1.firebasedatabase.app/image.json")
    suspend fun createImage(
        @Body imageRequest: ImageRequest
    ): Response<Map<String, String>>
}