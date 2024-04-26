package com.inter.planner.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inter.entity.Mapper
import com.inter.planner.datasources.LocalJourney
import com.inter.planner.datasources.RemoteJourney
import com.inter.entity.planner.ImageEntity
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.planner.dto.JourneyDTO
import com.inter.planner.dto.JourneyMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface JourneyRepository {

    suspend fun getListJourney()
    fun getListJourneyFlow(): Flow<List<com.inter.entity.planner.JourneyEntity>>
    fun getJourney(journeyId: String): Flow<com.inter.entity.planner.JourneyEntity>
    suspend fun migrateImages()
//    fun getPlaceJourney(journeyId: String): Flow<List<PlaceEntity>>
//    fun getJourney(): Flow<List<JourneyEntity>>
}

class JourneyRepositoryImpl @Inject constructor(
    val local: LocalJourney,
    val remote: RemoteJourney,
    val mapper: Mapper<JourneyEntity, JourneyDTO>
) :
    JourneyRepository {
    private val _listJourney: MutableLiveData<List<com.inter.entity.planner.JourneyEntity>> = MutableLiveData()
    val listJourney: LiveData<List<com.inter.entity.planner.JourneyEntity>> = _listJourney

    private val _listnumber: MutableLiveData<List<Int>> = MutableLiveData()
    val listnumber: LiveData<List<Int>> = _listnumber


    override suspend fun getListJourney() {
        val myListJourney: List<com.inter.entity.planner.JourneyEntity>? = local.getListJourney()

        withContext(Dispatchers.Main)
        {
            _listJourney.value = myListJourney
        }


        val listDTO=myListJourney?.map{
            return@map mapper.toData(it)
        }

        val tmp=listDTO?.size




        myListJourney?.forEach {
            val places: List<com.inter.entity.planner.PlaceEntity> = local.getJourneyPlaces(it.id).places.map {
                return@map com.inter.entity.planner.PlaceEntity(
                    it.id,
                    it.ref_journey_id,
                    it.timestamp,
                    it.title,
                    it.desc,
                    it.lat,
                    it.lon
                )
            }
            it.listPlaces = places

            places?.forEach { place ->
                val listOfImages = local.getPlaceImages(place.id).images

                place.listImage = listOfImages.map { lcal ->
                    return@map com.inter.entity.planner.ImageEntity(
                        lcal.id,
                        lcal.ref_place_id,
                        lcal.path,
                        ""
                    )
                }

            }
        }


        withContext(Dispatchers.Main)
        {
            _listJourney.value = myListJourney
        }


    }

    override fun getListJourneyFlow(): Flow<List<com.inter.entity.planner.JourneyEntity>> = flow {
        val myListJourney = local.getListJourney()
        myListJourney?.apply {
            emit(this)
        }

    }

    override fun getJourney(journeyId: String): Flow<com.inter.entity.planner.JourneyEntity> = flow<com.inter.entity.planner.JourneyEntity> {

        val journeyPlace = local.getJourneyPlaces(journeyId)
        val journey = journeyPlace.journey
        val places = journeyPlace.places.map {
            return@map com.inter.entity.planner.PlaceEntity(
                id = it.id,
                ref_journey_id = it.ref_journey_id,
                timestamp = it.timestamp,
                title = it.title,
                desc = it.desc,
                lat = it.lat,
                lon = it.lon,
                listImage = local.getPlaceImages(it.id).images.map { img ->
                    return@map com.inter.entity.planner.ImageEntity(
                        img.id,
                        img.ref_place_id,
                        img.path,
                        ""
                    )
                }
            )
        }

        emit(
            com.inter.entity.planner.JourneyEntity(
                journey.id,
                journey.timestamp,
                journey.title,
                journey.desc,
                places
            )
        )

    }.flowOn(Dispatchers.IO)
        .flowOn(Dispatchers.Main)


    override suspend fun migrateImages() {
        local.migrateImages()
    }


//    override fun getPlaceJourney(journeyId: String): Flow<List<PlaceEntity>> =
//        flow<List<PlaceEntity>> {
//            val journeyPlaces = local.getJourneyPlace(journeyId)
//            val places = journeyPlaces.places.map {
//                return@map PlaceEntity(
//                    it.id,
//                    it.ref_journey_id,
//                    it.timestamp,
//                    it.title,
//                    it.desc,
//                    it.lat,
//                    it.lon
//                )
//            }
//            _listJourney.value?.find {
//                it.id == journeyId
//            }?.apply {
//                this.listPlaces = places
//            }
//        }.flowOn(Dispatchers.IO)
//
//    override fun getJourney(): Flow<List<JourneyEntity>> = flow<List<JourneyEntity>> {
//        val myListJourney = local.getListJourney()
//        _listJourney.value = myListJourney
//    }

//    override fun getJourney(): Flow<List<JourneyEntity>> = flow<List<JourneyEntity>> {
//        val myListJourney = local.getListJourney()
//        withContext(Dispatchers.Main)
//        {
//            _listJourney.value = myListJourney
//        }
//        myListJourney?.apply {
//            emit(myListJourney)
//        }
//    }.flowOn(Dispatchers.IO)


}

