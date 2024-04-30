package com.inter.planner.datasources

import com.inter.database.CoreDatabase
import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.PlaceImages
import kotlinx.coroutines.delay
import javax.inject.Inject


class JourneyLocalDatasource @Inject constructor(val coreDatabase: CoreDatabase) {
    fun getListJourney(): List<com.inter.entity.planner.JourneyEntity>? {
        var ret = coreDatabase?.getListJourney()
        var rett = ret?.map {
            com.inter.entity.planner.JourneyEntity(it.id, it.timestamp, it.title, it.desc)
        }
        return rett
    }

    suspend fun getJourneyPlaces(journeyId: String): JourneyPlaces {
        delay(100)
        return coreDatabase.getJourneyPlaces(journeyId)
    }

    suspend fun getPlaceImages(placeId: String): PlaceImages {
        return coreDatabase.getPlaceImages(placeId)
    }


    suspend fun migrateImages() {
        return coreDatabase.migrateImages()
    }
}