package com.inter.planner.repositories

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inter.planner.datasources.JourneyLocalDatasource
import com.inter.planner.datasources.RemoteJourney
import com.inter.entity.planner.ImageEntity
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.entity.planner.ApiResult
import com.inter.planner.datasources.ImageLocalDataSource
import com.inter.planner.datasources.PlaceLocalDatasource
import com.inter.planner.datasources.RemoteImage
import com.inter.planner.datasources.RemotePlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface JourneyRepository {

    suspend fun getListJourney()
    fun getListJourneyFlow(): Flow<List<com.inter.entity.planner.JourneyEntity>>
    fun getJourney(journeyId: String): Flow<com.inter.entity.planner.JourneyEntity>
    suspend fun migrateImages()
//    fun getPlaceJourney(journeyId: String): Flow<List<PlaceEntity>>
//    fun getJourney(): Flow<List<JourneyEntity>>


    fun backupJourneyToServer(journeyEntity: JourneyEntity): Flow<ApiResult<JourneyEntity>>

    fun backupPlaceToServer(placeEntity: PlaceEntity): Flow<ApiResult<PlaceEntity>>


    suspend fun createJourney(journeyEntity: JourneyEntity): ApiResult<*>
    suspend fun deleteJourney(journeyId: String): ApiResult<*>

    suspend fun createPlace(placeEntity: PlaceEntity): PlaceEntity
    suspend fun deletePlace(placeId: String): String?
    suspend fun deleteImage(imageId: String): String

    suspend fun createImage(imageEntity: ImageEntity): ImageEntity

}

class JourneyRepositoryImpl @Inject constructor(
    val localJourney: JourneyLocalDatasource,
    val localPlaceDataSource: PlaceLocalDatasource,
    val localImageDatasource: ImageLocalDataSource,
    val remote: RemoteJourney,
    val remotePlace: RemotePlace,
    val remoteImage: RemoteImage,
) : JourneyRepository, Parcelable {
    private val _listJourney: MutableLiveData<MutableList<com.inter.entity.planner.JourneyEntity>> =
        MutableLiveData()
    val listJourney: LiveData<MutableList<com.inter.entity.planner.JourneyEntity>> = _listJourney

    private val _listnumber: MutableLiveData<List<Int>> = MutableLiveData()
    val listnumber: LiveData<List<Int>> = _listnumber

    constructor(parcel: Parcel) : this(
        TODO("local"),
        TODO("localPlaceDataSource"),
        TODO("localImageDatasource"),
        TODO("remote"),
        TODO("remotePlace"),
        TODO("remoteImage")
    ) {
    }

    override suspend fun getListJourney() {
        val myListJourney: MutableList<JourneyEntity>? = localJourney.getListJourney()
        withContext(Dispatchers.Main) {
            _listJourney.value = myListJourney
        }
        myListJourney?.forEach {
            val places: MutableList<PlaceEntity> = localJourney.getJourneyPlaces(it.id).places.map {
                return@map PlaceEntity(
                    it.id, it.ref_journey_id, it.timestamp, it.title, it.desc, it.lat, it.lon
                )
            }.toMutableList()
            it.listPlaces = places

            places?.forEach { place ->
                val listOfImages = localJourney.getPlaceImages(place.id).images
                place.listImage = listOfImages.map { lcal ->
                    return@map ImageEntity(
                        lcal.id, lcal.ref_place_id, lcal.path, ""
                    )
                }.toMutableList()
            }
        }

        withContext(Dispatchers.Main) {
            _listJourney.value = myListJourney
        }

    }

    override fun getListJourneyFlow(): Flow<List<JourneyEntity>> = flow {
        val myListJourney = localJourney.getListJourney()
        myListJourney?.apply {
            emit(this)
        }

    }

    override fun getJourney(journeyId: String): Flow<JourneyEntity> = flow<JourneyEntity> {

        val journeyPlace = localJourney.getJourneyPlaces(journeyId)
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
                listImage = localJourney.getPlaceImages(it.id).images.map { img ->
                    return@map ImageEntity(
                        img.id, img.ref_place_id, img.path, ""
                    )
                }.toMutableList()
            )
        }.toMutableList()

        emit(
            JourneyEntity(
                journey.id, journey.timestamp, journey.title, journey.desc, places
            )
        )

    }.flowOn(Dispatchers.IO)


    override suspend fun migrateImages() {
        localJourney.migrateImages()
    }

    override fun backupJourneyToServer(journeyEntity: JourneyEntity): Flow<ApiResult<JourneyEntity>> =
        flow<ApiResult<JourneyEntity>> {

            val ret = remote.createJourney(journeyEntity)
            if (ret.isSuccessful) {
                val body: Map<String, String>? = ret.body()
                body?.apply {
                    val id = this.get("name")
                    if (null != id) {

                        val tmpJourney = JourneyEntity(
                            id = journeyEntity.id,
                            timestamp = journeyEntity.timestamp,
                            title = journeyEntity.title,
                            desc = journeyEntity.desc,
                            listPlaces = mutableListOf()
                        )
                        emit(ApiResult.Success<JourneyEntity>(tmpJourney))

                        journeyEntity.listPlaces?.forEach { place ->
                            val retPlaces = remotePlace.createPlace(place)
                            if (retPlaces.isSuccessful) {

                                retPlaces.body()?.apply {
                                    if (null != this.get("name")) {

                                        tmpJourney.listPlaces.add(place)

                                        place.listImage.forEach { img ->
                                            val retImage = remoteImage.createImage(img)
                                            if (retImage.isSuccessful) {
                                                val bodyImage: Map<String, String>? =
                                                    retImage.body()
                                                bodyImage?.apply {
                                                    val imgId = this.get("name")
                                                    if (null != imgId) {
                                                        place.listImage.add(img)

                                                        emit(
                                                            ApiResult.Success<JourneyEntity>(
                                                                tmpJourney
                                                            )
                                                        )
                                                    }
                                                }
                                            } else {
                                                retImage.toString()
                                            }
                                        }

                                    } else {

                                    }
                                }
                            }
                        }
                        emit(ApiResult.Success<JourneyEntity>(tmpJourney))


                    } else {
                        emit(ApiResult.Error("Error"))
                    }
                }
            } else {
                emit(ApiResult.Error("Error"))
            }

        }.flowOn(Dispatchers.IO).onStart {
            emit(ApiResult.Loading(true))
        }.catch {
            emit(ApiResult.Loading(true))
        }

    override fun backupPlaceToServer(placeEntity: PlaceEntity): Flow<ApiResult<PlaceEntity>> =
        flow {

        }

    override suspend fun createJourney(journeyEntity: JourneyEntity): ApiResult<*> {

        val ret = localJourney.createJourney(journeyEntity)
        if (ret is ApiResult.Success) {
            val entiry = ret.data as JourneyEntity
            val tmp = listJourney.value
            tmp?.let {
                it.add(0, entiry)
                withContext(Dispatchers.Main)
                {
                    _listJourney.value = it
                }

            }
            return ApiResult.Success<String>("Create success")
        }

        return ret
    }


    override suspend fun deleteJourney(journeyId: String): ApiResult<*> {
        try {
            val ret = localJourney.deleteJourney(journeyId)
            val tmp = listJourney.value?.filter {
                it.id != journeyId
            }?.toMutableList()
            withContext(Dispatchers.Main)
            {
                _listJourney.value = tmp
            }

            return ApiResult.Success("Success")
        } catch (e: Exception) {
            return ApiResult.Error<Exception>(e)
        }


    }

    override suspend fun createPlace(placeEntity: PlaceEntity): PlaceEntity {
        return localPlaceDataSource.createPlace(placeEntity)
    }

    override suspend fun deletePlace(placeId: String): String {
        return localPlaceDataSource.deletePlace(placeId)
    }

    override suspend fun deleteImage(imageId: String): String {
        return localImageDatasource.deletImage(imageId)
    }

    override suspend fun createImage(imageEntity: ImageEntity): ImageEntity {
        return localImageDatasource.createImage(imageEntity)
    }


//    override fun createImage(image: RemoteImage) {
//
//            val threadName = Thread.currentThread().name
//            Log.d("THREADING_COROUTIN", threadName)
//
//    }


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
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JourneyRepositoryImpl> {
        override fun createFromParcel(parcel: Parcel): JourneyRepositoryImpl {
            return JourneyRepositoryImpl(parcel)
        }

        override fun newArray(size: Int): Array<JourneyRepositoryImpl?> {
            return arrayOfNulls(size)
        }
    }


}

