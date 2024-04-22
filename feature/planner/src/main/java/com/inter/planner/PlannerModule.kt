package com.inter.planner

import com.inter.planner.datasources.LocalJourney
import com.inter.planner.datasources.RemoteJourney
import com.inter.planner.repositories.JourneyRepository
import com.inter.planner.repositories.JourneyRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PlannerModule {
    @Provides
    @Singleton
    fun providePlaceRepo(local: LocalJourney, remote: RemoteJourney): JourneyRepository {
        return JourneyRepositoryImpl(local, remote)
    }

}

