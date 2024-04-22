package com.inter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inter.database.entities.LocalImageTmp

@Dao
abstract class ImageDao {
    @Query("SELECT * FROM LocalImageTable")
    abstract fun getListLocalImage(): List<LocalImageTmp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertImage(localImage: LocalImageTmp)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateImage(localImage: LocalImageTmp)

    @Query("SELECT * FROM LocalImageTable WHERE place_id=:placeId")
    abstract fun getImagesOnPlace(placeId: String): List<LocalImageTmp>

    @Query("DELETE FROM LocalImageTable WHERE id=:imgId")
    abstract fun deleteImage(imgId: String)
}
