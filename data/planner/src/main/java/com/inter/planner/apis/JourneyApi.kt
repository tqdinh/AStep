package com.inter.network.apis

import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.JourneyResponse
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface JourneyApi {
    @POST("https://onestep-4004e-default-rtdb.asia-southeast1.firebasedatabase.app/journey.json")
    suspend fun createJourney(
        @Body journeyRequest: JourneyRequest
    ): Response<Map<String,String>>

}