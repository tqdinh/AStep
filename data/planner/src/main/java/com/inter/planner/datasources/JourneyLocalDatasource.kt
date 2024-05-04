package com.inter.planner.datasources

import com.inter.database.CoreDatabase
import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.LocalJourney
import com.inter.database.entities.PlaceImages
import com.inter.entity.DomainDbMapper
import com.inter.entity.planner.ApiResult
import com.inter.entity.planner.JourneyEntity
import kotlinx.coroutines.delay
import javax.inject.Inject


class JourneyLocalDatasource @Inject constructor(
    val coreDatabase: CoreDatabase,
    val mapper: DomainDbMapper<JourneyEntity, LocalJourney>
) {
    fun getListJourney(): MutableList<com.inter.entity.planner.JourneyEntity>? {
        var ret = coreDatabase?.getListJourney()
        var rett = ret?.map {
            com.inter.entity.planner.JourneyEntity(it.id, it.timestamp, it.title, it.desc)
        }?.toMutableList()
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

    suspend fun createJourney(journeyEntity: JourneyEntity): ApiResult<*> {
        return try {
            val localJourney = mapper.toDatabase(journeyEntity)
            coreDatabase.createJourney(localJourney)
            return ApiResult.Success(journeyEntity)
        } catch (e: Exception) {
            return ApiResult.Error(e)
        }

    }

    suspend fun deleteJourney(journeyId: String){
        return  coreDatabase.deleteJourney(journeyId)
    }
}