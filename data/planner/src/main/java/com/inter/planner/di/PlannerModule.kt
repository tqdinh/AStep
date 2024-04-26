package com.inter.planner.di

import com.inter.entity.Mapper
import com.inter.entity.planner.JourneyEntity
import com.inter.planner.datasources.LocalJourney
import com.inter.planner.datasources.RemoteJourney
import com.inter.planner.dto.JourneyDTO
import com.inter.planner.dto.JourneyMapper
import com.inter.planner.repositories.JourneyRepository
import com.inter.planner.repositories.JourneyRepositoryImpl
import dagger.Binds
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
    fun providePlaceRepo(
        local: LocalJourney,
        remote: RemoteJourney,
        mapper: Mapper<JourneyEntity, JourneyDTO>
    ): JourneyRepository {
        return JourneyRepositoryImpl(local, remote, mapper)
    }

    @Provides
    @Singleton
    fun provideJourneyMapper():Mapper<JourneyEntity, JourneyDTO>
    {
        return JourneyMapper()
    }

}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class Planner {
//    @Binds
//    abstract fun bindJourneyMapper(journeyMapper: JourneyMapper): Mapper<JourneyEntity, JourneyDTO>
//}
