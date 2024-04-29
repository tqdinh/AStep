package com.inter.planner.dto

import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.ImageEntity
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.planner.apis.request.ImageRequest
import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.PlaceRequest

class ImageDomainRequestMapper : DomainRequestMapper<ImageEntity, ImageRequest> {
    override fun toRequest(domain: ImageEntity): ImageRequest {
        return ImageRequest(

            id = domain.id,
            place_id = domain.ref_place_id,
            path = domain.path,
            url = domain.url
        )

    }
}