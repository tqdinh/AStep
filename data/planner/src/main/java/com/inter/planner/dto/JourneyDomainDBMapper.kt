package com.inter.planner.dto

import com.inter.database.entities.LocalJourney
import com.inter.entity.DomainDataMapper
import com.inter.entity.DomainDbMapper
import com.inter.entity.planner.JourneyEntity

class JourneyDomainDBMapper : DomainDbMapper<JourneyEntity, LocalJourney> {
    //    override fun toDomain(data: JourneyDTO): JourneyEntity {
//        return JourneyEntity(data.id, data.timestamp, data.title, data.desc, data.listPlaces)
//    }
//
//    override fun toData(domain: JourneyEntity): JourneyDTO {
//        return JourneyDTO(domain.id, domain.timestamp, domain.title, domain.desc, domain.listPlaces)
//    }
    override fun toDomain(db: LocalJourney): JourneyEntity {
        TODO("Not yet implemented")
    }

    override fun toDatabase(domain: JourneyEntity): LocalJourney {
        return LocalJourney(id=domain.id, timestamp = domain.timestamp, title = domain.title, desc = domain.desc)
    }
}