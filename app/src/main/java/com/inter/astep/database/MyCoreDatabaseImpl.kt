package com.inter.astep.database

import com.inter.database.CoreDatabase
import com.inter.database.MyRoomDatabase
import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.LocalImage
import com.inter.database.entities.LocalJourney
import com.inter.database.entities.LocalPlace
import com.inter.database.entities.PlaceImages

class MyCoreDatabaseImpl(val myRoomDatabase: MyRoomDatabase) : CoreDatabase {
    override fun getListJourney(): List<LocalJourney> {
        return try {
            myRoomDatabase.getLocalJourneyDao().getListJourney()
        } catch (e: Exception) {
            emptyList<LocalJourney>()
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

    override fun createJourney(journey: LocalJourney) {
        return try {
            myRoomDatabase.getLocalJourneyDao().insertLocalJourney(journey)
        }
        catch (e:Exception)
        {

        }

    }

    override fun deleteJourney(journeyId: String) {
        return myRoomDatabase.getLocalJourneyDao().deleteJourney(journeyId)
    }

    override fun createPlace(placeEntity: LocalPlace) {
        val kkk = myRoomDatabase.getLocalPlaceDao().insertPlace(placeEntity)
        return kkk
    }

    override fun deletePlace(placeId: String) {
        myRoomDatabase.getLocalPlaceDao().deletePlace(placeId)
    }

    override fun createImage(image: LocalImage) {
        val kkk = myRoomDatabase.getLocalImageDao().insertImage(image)
        return kkk
    }

    override fun deleteImage(imageId: String) {
        myRoomDatabase.getLocalImageDao().deleteImage(imageId)
    }

//    override fun getJourneyPlaces(): JourneyPlaces {
//        return
//    }
}