package com.inter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inter.database.dao.LocalImageDao
import com.inter.database.dao.LocalJourneyDao
import com.inter.database.dao.LocalPlaceDao
import com.inter.database.entities.LocalImage
import com.inter.database.entities.LocalJourney
import com.inter.database.entities.LocalPlace

@Database(
    entities = [LocalJourney::class, LocalPlace::class, LocalImage::class],
    version = 3
)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun getLocalPlaceDao(): LocalPlaceDao
    abstract fun getLocalJourneyDao(): LocalJourneyDao
    abstract fun getLocalImageDao(): LocalImageDao

}
