package com.inter.network.apis

import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.PlaceRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PlaceApi {

    @POST("https://onestep-4004e-default-rtdb.asia-southeast1.firebasedatabase.app/place.json")
    suspend fun createPlace(
        @Body placeRequest: PlaceRequest
    ): Response<Map<String, String>>
}