package com.inter.planner.dto

import com.inter.database.entities.LocalImage
import com.inter.database.entities.LocalPlace
import com.inter.entity.DomainDbMapper
import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.ImageEntity
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.PlaceRequest

class ImageDomainDBMapper : DomainDbMapper<ImageEntity, LocalImage> {
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
    override fun toDomain(db: LocalImage): ImageEntity {
        return ImageEntity(
            id = db.id, ref_place_id = db.ref_place_id, url = db.url, path = db.path
        )
    }

    override fun toDatabase(domain: ImageEntity): LocalImage {
        return LocalImage(
            id = domain.id,
            ref_place_id = domain.ref_place_id,
            path = domain.path,
            url = domain.url
        )
    }
}