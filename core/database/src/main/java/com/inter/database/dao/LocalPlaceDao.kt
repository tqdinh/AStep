package com.inter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inter.database.entities.LocalPlace
import com.inter.database.entities.PlaceImages


@Dao
abstract class LocalPlaceDao {
    @Query("SELECT * FROM place_table")
    abstract fun getListPlace(): List<LocalPlace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlace(localImage: LocalPlace)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatePlace(localImage: LocalPlace)

    @Query("SELECT * FROM place_table WHERE place_id=:placeId ")
    abstract fun getImageOfPlace(placeId: String): PlaceImages


    @Query("DELETE FROM place_table WHERE place_id=:placeId")
    abstract fun deletePlace(placeId: String)
//
//    @Query("SELECT * FROM place_table WHERE place_id=:placeId")
//    abstract fun getImagesOnPlace(placeId: String): List<LocalPlaceEntity>

//    @Query("DELETE FROM place_table WHERE id=:imgId")
//    abstract fun deleteImage(imgId: String)
}
