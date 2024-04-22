package com.inter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inter.database.entities.LocalPlaceEntity
import com.inter.database.entities.PlaceImages


@Dao
abstract class LocalPlaceDao {
    @Query("SELECT * FROM place_table")
    abstract fun getListPlace(): List<LocalPlaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPlace(localImage: LocalPlaceEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatePlace(localImage: LocalPlaceEntity)

    @Query("SELECT * FROM place_table WHERE place_id=:placeId ")
    abstract fun getImageOfPlace(placeId:String): PlaceImages
//
//    @Query("SELECT * FROM place_table WHERE place_id=:placeId")
//    abstract fun getImagesOnPlace(placeId: String): List<LocalPlaceEntity>

//    @Query("DELETE FROM place_table WHERE id=:imgId")
//    abstract fun deleteImage(imgId: String)
}
