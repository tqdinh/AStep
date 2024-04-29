package com.inter.planner.datasources

import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.ImageEntity
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.network.apis.ImageApi
import com.inter.network.apis.JourneyApi
import com.inter.network.apis.PlaceApi
import com.inter.planner.apis.request.ImageRequest
import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.JourneyResponse
import com.inter.planner.apis.request.PlaceRequest
import com.inter.planner.dto.PlaceDomainRequestMapper
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class RemoteImage @Inject constructor(
    private val imageApi: ImageApi,
    private val domainRquestMapper: DomainRequestMapper<ImageEntity, ImageRequest>
) {
    suspend fun createImage(imageEntity: ImageEntity): Response<Map<String, String>> {
        val imageRequest = domainRquestMapper.toRequest(imageEntity)
        return imageApi.createImage(imageRequest)
    }
}