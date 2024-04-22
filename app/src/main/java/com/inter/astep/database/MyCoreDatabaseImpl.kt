package com.inter.astep.database

import com.inter.database.CoreDatabase
import com.inter.database.MyRoomDatabase
import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.LocalJourneyEntity
import com.inter.database.entities.PlaceImages

class MyCoreDatabaseImpl(val myRoomDatabase: MyRoomDatabase) : CoreDatabase {
    override fun getListJourney(): List<LocalJourneyEntity> {
        return try {
            myRoomDatabase.getLocalJourneyDao().getListJourney()
        } catch (e: Exception) {
            emptyList<LocalJourneyEntity>()
        }

    }

    override fun getJourneyPlaces(journeyId: String): JourneyPlaces {
        return myRoomDatabase.getLocalJourneyDao().getPlacesOfJourney(journeyId)
    }

    override fun getPlaceImages(placeId: String): PlaceImages {
        val tmp = myRoomDatabase.getLocalPlaceDao().getImageOfPlace(placeId)
        return tmp
    }

    override fun getJourneyWithId(journeyId: String): JourneyPlaces {
        return myRoomDatabase.getLocalJourneyDao().getPlacesOfJourney(journeyId)
    }

    override fun migrateImages() {
        val listOfImages = myRoomDatabase.getLocalImageDao().getListLocalImage()
        for (img in listOfImages) {
            val old_path = img.path
            var new_path = old_path.replace("com.ocsen.onestep/dinh_app", "com.inter.astep/images")

            myRoomDatabase.getLocalImageDao().updatePathOfApp(new_path, img.id)
        }
    }

//    override fun getJourneyPlaces(): JourneyPlaces {
//        return
//    }
}