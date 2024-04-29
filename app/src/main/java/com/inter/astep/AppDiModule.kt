package com.inter.main.di


import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration

import com.inter.astep.database.MyCoreDatabaseImpl
import com.inter.database.CoreDatabase
import com.inter.database.MyRoomDatabase


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDiModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(application: Application): MyRoomDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            MyRoomDatabase::class.java,
            "place_info.db"
        ).createFromAsset("place_info.db")
//            .addMigrations(Migration(3, 4, {
//
//            }))
            .build()
    }

    @Provides
    @Singleton
    fun provideCoreDatabaseImpl(myRoomDatabase: MyRoomDatabase): CoreDatabase {
        return MyCoreDatabaseImpl(myRoomDatabase)

    }


}