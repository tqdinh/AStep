package com.inter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inter.database.dao.LocalImageDao
import com.inter.database.dao.LocalJourneyDao
import com.inter.database.dao.LocalPlaceDao
import com.inter.database.entities.LocalImageEntity
import com.inter.database.entities.LocalImageTmp
import com.inter.database.entities.LocalJourneyEntity
import com.inter.database.entities.LocalPlaceEntity

@Database(
    entities = [ LocalJourneyEntity::class, LocalPlaceEntity::class, LocalImageEntity::class],
    version = 3
)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun getLocalPlaceDao(): LocalPlaceDao
    abstract fun getLocalJourneyDao(): LocalJourneyDao
    abstract fun getLocalImageDao(): LocalImageDao

}
