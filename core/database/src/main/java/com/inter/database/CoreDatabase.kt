package com.inter.database

import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.LocalImage
import com.inter.database.entities.LocalJourney
import com.inter.database.entities.LocalPlace
import com.inter.database.entities.PlaceImages

interface CoreDatabase {
    fun getListJourney(): List<LocalJourney>
    fun getJourneyPlaces(journeyId: String): JourneyPlaces
    fun getPlaceImages(placeId: String): PlaceImages
    fun getJourneyWithId(journeyId: String): JourneyPlaces

    fun migrateImages()

    fun createJourney(journey: LocalJourney)
    fun deleteJourney(journeyId: String)

    fun createPlace(place: LocalPlace)
    fun deletePlace(placeId: String)

    fun createImage(image: LocalImage)
    fun deleteImage(imageId: String)
}