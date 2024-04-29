package com.inter.planner.di

import com.inter.entity.DomainDataMapper
import com.inter.entity.DomainRequestMapper
import com.inter.entity.planner.ImageEntity
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.network.NetworkUtils
import com.inter.network.apis.ImageApi
import com.inter.network.apis.JourneyApi
import com.inter.network.apis.PlaceApi
import com.inter.planner.apis.request.ImageRequest
import com.inter.planner.apis.request.JourneyRequest
import com.inter.planner.apis.request.PlaceRequest
import com.inter.planner.datasources.LocalJourney
import com.inter.planner.datasources.RemoteImage
import com.inter.planner.datasources.RemoteJourney
import com.inter.planner.datasources.RemotePlace
import com.inter.planner.dto.ImageDomainRequestMapper
import com.inter.planner.dto.JourneyDTO
import com.inter.planner.dto.JourneyDomainDataMapper
import com.inter.planner.dto.JourneyDomainRequestMapper
import com.inter.planner.dto.PlaceDomainRequestMapper
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
    fun providePlaceRepo(
        local: LocalJourney,
        remote: RemoteJourney,
        remotePlace: RemotePlace,
        remoteImage: RemoteImage
    ): JourneyRepository {
        return JourneyRepositoryImpl(local, remote, remotePlace,remoteImage)
    }

    @Provides
    @Singleton
    fun provideJourneyMapper(): DomainDataMapper<JourneyEntity, JourneyDTO> {
        return JourneyDomainDataMapper()
    }

    @Provides
    @Singleton
    fun provideJourneyDomainRequestMapper(): DomainRequestMapper<JourneyEntity, JourneyRequest> {
        return JourneyDomainRequestMapper()
    }

    @Provides
    @Singleton
    fun providePlaceDomainRequestMapper(): DomainRequestMapper<PlaceEntity, PlaceRequest> {
        return PlaceDomainRequestMapper()
    }

    @Provides
    @Singleton
    fun provideImageDomainRequestMapper(): DomainRequestMapper<ImageEntity, ImageRequest> {
        return ImageDomainRequestMapper()
    }

    @Provides
    @Singleton
    fun provideJourneyApi(): JourneyApi {
        return NetworkUtils.getRetrofit().create(JourneyApi::class.java)

    }

    @Provides
    @Singleton
    fun providePlaceApi(): PlaceApi {
        return NetworkUtils.getRetrofit().create(PlaceApi::class.java)

    }

    @Provides
    @Singleton
    fun provideImageApi(): ImageApi =
        NetworkUtils.getRetrofit().create(ImageApi::class.java)
}

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class Planner {
//    @Binds
//    abstract fun bindJourneyMapper(journeyMapper: JourneyMapper): Mapper<JourneyEntity, JourneyDTO>
//}
