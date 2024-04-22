package com.inter.planner.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inter.planner.datasources.LocalJourney
import com.inter.planner.datasources.RemoteJourney
import com.inter.planner.entity.ImageEntity
import com.inter.planner.entity.JourneyEntity
import com.inter.planner.entity.PlaceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface JourneyRepository {

    suspend fun getListJourney()
    fun getJourney(journeyId: String): Flow<JourneyEntity>
    suspend fun migrateImages()
//    fun getPlaceJourney(journeyId: String): Flow<List<PlaceEntity>>
//    fun getJourney(): Flow<List<JourneyEntity>>
}

class JourneyRepositoryImpl @Inject constructor(
    val local: LocalJourney,
    val remote: RemoteJourney
) :
    JourneyRepository {
    private val _listJourney: MutableLiveData<List<JourneyEntity>> = MutableLiveData()
    val listJourney: LiveData<List<JourneyEntity>> = _listJourney


    override suspend fun getListJourney() {
        val myListJourney = local.getListJourney()

        withContext(Dispatchers.Main) {
            _listJourney.value = myListJourney
        }

        myListJourney?.forEach {
            val journeyId = it.id
            val journeyPlaces = local.getJourneyPlaces(it.id)
            val places = journeyPlaces.places.map {
                return@map PlaceEntity(
                    it.id,
                    it.ref_journey_id,
                    it.timestamp,
                    it.title,
                    it.desc,
                    it.lat,
                    it.lon
                )
            }

            _listJourney.value?.find {
                it.id == journeyId
            }?.apply {
                withContext(Dispatchers.Main) {
                    it.listPlaces = places
                }
                this.listPlaces?.forEach { place ->
                    val listOfImages = local.getPlaceImages(place.id).images

                    place.listImage = listOfImages.map { lcal ->
                        return@map ImageEntity(lcal.id, lcal.ref_place_id, lcal.path, "")
                    }
                    //place =listOfImages
                }
            }


        }
    }

    override fun getJourney(journeyId: String): Flow<JourneyEntity> = flow<JourneyEntity> {

        val journeyPlace = local.getJourneyPlaces(journeyId)
        val journey = journeyPlace.journey
        val places = journeyPlace.places.map {
            return@map PlaceEntity(
                id = it.id,
                ref_journey_id = it.ref_journey_id,
                timestamp = it.timestamp,
                title = it.title,
                desc = it.desc,
                lat = it.lat,
                lon = it.lon,
                listImage = local.getPlaceImages(it.id).images.map { img ->
                    return@map ImageEntity(img.id, img.ref_place_id, img.path, "")
                }
            )
        }

        emit(JourneyEntity(journey.id, journey.timestamp, journey.title, journey.desc, places))

    }.flowOn(Dispatchers.IO)


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