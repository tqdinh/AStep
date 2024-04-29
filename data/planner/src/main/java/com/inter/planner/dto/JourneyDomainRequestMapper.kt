package com.inter.planner.dto

import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.JourneyEntity
import com.inter.planner.apis.request.JourneyRequest

class JourneyDomainRequestMapper : DomainRequestMapper<JourneyEntity, JourneyRequest> {
    override fun toRequest(domain: JourneyEntity): JourneyRequest {
        return JourneyRequest(
            id = domain.id,
            created_time = domain.timestamp,
            updated_time = domain.timestamp,
            title = domain.title,
            desc = domain.desc
        )

    }
}