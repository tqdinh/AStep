package com.inter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inter.database.entities.JourneyPlaces
import com.inter.database.entities.LocalJourney


@Dao
abstract class LocalJourneyDao {
    @Query("SELECT * FROM journey_table")
    abstract fun getListJourney(): List<LocalJourney>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocalJourney(place: LocalJourney)

    @Query("SELECT * FROM journey_table WHERE journey_id =:journeyId")
    abstract fun getPlacesOfJourney(journeyId: String): JourneyPlaces

    @Query("SELECT * FROM journey_table")
    abstract fun getJourneyPlaces():List<JourneyPlaces>


    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateJourney(place: LocalJourney)


    @Query("DELETE FROM journey_table WHERE journey_id=:journeyId")
    abstract fun deleteJourney(journeyId:String)
}