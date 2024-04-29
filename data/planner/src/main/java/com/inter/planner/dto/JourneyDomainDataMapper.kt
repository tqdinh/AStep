package com.inter.planner.dto

import com.inter.entity.DomainDataMapper
import com.inter.entity.planner.JourneyEntity

class JourneyDomainDataMapper : DomainDataMapper<JourneyEntity, JourneyDTO> {
    override fun toDomain(data: JourneyDTO): JourneyEntity {
        return JourneyEntity(data.id, data.timestamp, data.title, data.desc, data.listPlaces)
    }

    override fun toData(domain: JourneyEntity): JourneyDTO {
        return JourneyDTO(domain.id, domain.timestamp, domain.title, domain.desc, domain.listPlaces)
    }
}