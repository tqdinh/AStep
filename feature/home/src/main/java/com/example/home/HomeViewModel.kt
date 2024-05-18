package com.example.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inter.entity.planner.ApiResult
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.planner.repositories.JourneyRepository
import com.inter.planner.repositories.JourneyRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: JourneyRepository) : ViewModel() {

    val listJourney: LiveData<MutableList<JourneyEntity>> =
        (repository as JourneyRepositoryImpl).listJourney

    val listnumber: LiveData<List<Int>> = (repository as JourneyRepositoryImpl).listnumber

    fun getJourney() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListJourney()
        }
    }

    suspend fun createJourney(journeyName: String, journeyDesc: String): Flow<ApiResult<*>> = flow {
        try {
            val journeyEntity = JourneyEntity(
                id = UUID.randomUUID().toString(),
                timestamp = System.currentTimeMillis(),
                desc = journeyDesc,
                title = journeyName
            )
            repository.createJourney(journeyEntity) as ApiResult
            emit(ApiResult.Success<Boolean>(true))
        } catch (e: Exception) {
            emit(ApiResult.Error<Exception>(e))
            e.toString()
        } finally {
            emit(ApiResult.Loading(false))
        }
    }.onStart {
        emit(ApiResult.Loading(true))
    }

    suspend fun deleteJourney(journeyId: String): Flow<ApiResult<*>> = flow<ApiResult<*>> {
        try {

            repository.deleteJourney(journeyId)
            emit(ApiResult.Success<Boolean>(true))
        } catch (e: Exception) {
            emit(ApiResult.Error<Exception>(e))
            e.toString()

        } finally {
            emit(ApiResult.Loading(false))
        }

    }.flowOn(Dispatchers.IO)
        .onStart {
            emit(ApiResult.Loading(true))
        }


    fun migrate() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.migrateImages()
        }
    }

    fun getPlaceOfJourney(journeyId: String) {

    }

}


