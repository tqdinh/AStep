package com.inter.planner.datasources

import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.network.apis.JourneyApi
import com.inter.network.apis.PlaceApi
import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.JourneyResponse
import com.inter.planner.apis.request.PlaceRequest
import com.inter.planner.dto.PlaceDomainRequestMapper
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RemotePlace @Inject constructor(
    val placeApi: PlaceApi,
    val domainRquestMapper: DomainRequestMapper<PlaceEntity, PlaceRequest>
) {
    suspend fun createPlace(placeEntity: PlaceEntity): Response<Map<String, String>> {
        val placeRequest = domainRquestMapper.toRequest(placeEntity)
        return placeApi.createPlace(placeRequest)
    }
}