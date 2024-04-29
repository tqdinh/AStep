package com.inter.planner.datasources

import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.JourneyEntity
import com.inter.network.apis.JourneyApi
import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.JourneyResponse
import com.inter.planner.apis.request.PlaceRequest
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RemoteJourney @Inject constructor(
    val journeyApi: JourneyApi,
    val domainRquestMapper: DomainRequestMapper<JourneyEntity, JourneyRequest>
) {
    suspend fun createJourney(journeyEntity: JourneyEntity): Response<Map<String, String>> {
        val request = domainRquestMapper.toRequest(journeyEntity)
        return journeyApi.createJourney(request)
    }
}