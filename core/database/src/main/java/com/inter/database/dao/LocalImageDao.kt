package com.inter.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inter.database.entities.LocalImageEntity


@Dao
abstract class LocalImageDao {
    @Query("SELECT * FROM image_table")
    abstract fun getListLocalImage(): List<LocalImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertImage(localImage: LocalImageEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateImage(localImage: LocalImageEntity)

    @Query("UPDATE image_table SET path=:new_path WHERE img_id=:imageId")
    abstract fun updatePathOfApp(new_path:String,imageId:String)


//    @Query("SELECT * FROM image_table WHERE place_id=:placeId")
//    abstract fun getImagesOnPlace(placeId: String): List<LocalImageEntity>
//
//    @Query("DELETE FROM image_table WHERE id=:imgId")
//    abstract fun deleteImage(imgId: String)
}
