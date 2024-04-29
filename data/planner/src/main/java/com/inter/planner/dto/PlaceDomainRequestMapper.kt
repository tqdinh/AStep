package com.inter.planner.dto

import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.PlaceRequest

class PlaceDomainRequestMapper : DomainRequestMapper<PlaceEntity, PlaceRequest> {
    override fun toRequest(domain: PlaceEntity): PlaceRequest {
        return PlaceRequest(

            id = domain.id,
            journey_id = domain.ref_journey_id,
            created_time = domain.timestamp,
            updated_time = domain.timestamp,
            title = domain.title,
            desc = domain.desc,
            lat = domain.lat,
            lon = domain.lon
        )

    }
}