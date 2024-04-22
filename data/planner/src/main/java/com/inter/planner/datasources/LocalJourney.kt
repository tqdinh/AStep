package com.inter.planner.datasources

import com.inter.database.CoreDatabase
import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.PlaceImages
import com.inter.planner.entity.JourneyEntity
import javax.inject.Inject


class LocalJourney @Inject constructor(val coreDatabase: CoreDatabase) {
    fun getListJourney(): List<JourneyEntity>? {
        var ret = coreDatabase?.getListJourney()
        var rett = ret?.map {
            JourneyEntity(it.id, it.timestamp, it.title, it.desc)
        }
        return rett
    }

    suspend fun getJourneyPlaces(journeyId: String): JourneyPlaces {
        return coreDatabase.getJourneyPlaces(journeyId)
    }

    suspend fun getPlaceImages(placeId: String): PlaceImages {
        return coreDatabase.getPlaceImages(placeId)
    }


    suspend fun migrateImages() {
        return coreDatabase.migrateImages()
    }
}