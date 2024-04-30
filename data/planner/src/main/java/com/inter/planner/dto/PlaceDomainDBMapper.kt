package com.inter.planner.dto

import com.inter.database.entities.LocalPlace
import com.inter.entity.DomainDbMapper
import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.PlaceRequest

class PlaceDomainDBMapper : DomainDbMapper<PlaceEntity, LocalPlace> {
    //    override fun toRequest(domain: PlaceEntity): PlaceRequest {
//        return PlaceRequest(
//
//            id = domain.id,
//            journey_id = domain.ref_journey_id,
//            created_time = domain.timestamp,
//            updated_time = domain.timestamp,
//            title = domain.title,
//            desc = domain.desc,
//            lat = domain.lat,
//            lon = domain.lon
//        )
//
//    }
    override fun toDomain(db: LocalPlace): PlaceEntity {
        return PlaceEntity(
            db.id, db.ref_journey_id, db.timestamp, db.title, db.desc, db.lat, db.lon,
            mutableListOf()
        )
    }

    override fun toDatabase(domain: PlaceEntity): LocalPlace {
        return LocalPlace(
            domain.id,
            domain.ref_journey_id,
            domain.timestamp,
            domain.title,
            domain.desc,
            domain.lat,
            domain.lon
        )
    }
}