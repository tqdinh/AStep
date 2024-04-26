package com.inter.database

import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.LocalJourney
import com.inter.database.entities.PlaceImages

interface CoreDatabase {
    fun getListJourney(): List<LocalJourney>
    fun getJourneyPlaces(journeyId: String): JourneyPlaces
    fun getPlaceImages(placeId: String): PlaceImages
    fun getJourneyWithId(journeyId: String): JourneyPlaces

    fun migrateImages()
}