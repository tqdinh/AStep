package com.inter.database

import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.LocalJourneyEntity
import com.inter.database.entities.PlaceImages

interface CoreDatabase {
    fun getListJourney(): List<LocalJourneyEntity>
    fun getJourneyPlaces(journeyId: String): JourneyPlaces
    fun getPlaceImages(placeId: String): PlaceImages
    fun getJourneyWithId(journeyId: String): JourneyPlaces

    fun migrateImages()
}