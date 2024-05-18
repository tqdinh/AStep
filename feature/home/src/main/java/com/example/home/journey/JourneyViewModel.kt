package com.example.home.journey

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inter.entity.planner.ApiResult
import com.inter.entity.planner.PlaceEntity
import com.inter.mylocation.BackgroundLocationService
import com.inter.planner.repositories.JourneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JourneyViewModel @Inject constructor(val repository: JourneyRepository) : ViewModel() {


    var mBinder: BackgroundLocationService.BackgroundLocationBindder? = null
//    fun setBindService(binder: BackgroundLocationService.BackgroundLocationBindder) {
//        mBinder = binder
//    }

    private val _journey: MutableLiveData<com.inter.entity.planner.JourneyEntity> =
        MutableLiveData()
    val journey: LiveData<com.inter.entity.planner.JourneyEntity> = _journey

    private val _syncJourney: MutableLiveData<Boolean> = MutableLiveData()
    val syncJourney: LiveData<Boolean> = _syncJourney
    fun getJourney(journeyId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getJourney(journeyId).collect()
            {
                withContext(Dispatchers.Main)
                {
                    _journey.value = it
                }

            }
        }

    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: BackgroundLocationService.BackgroundLocationBindder =
                service as BackgroundLocationService.BackgroundLocationBindder
            mBinder = binder
            if (null != binder.getJourney())
                _syncJourney.value = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    fun isTracking(): Boolean {

        var ret = false

        mBinder?.apply {
            ret = this.getJourney() != null
        }

        return ret
    }


    fun setDataForbindService() {
        mBinder?.apply {

            if (null != journey.value) {
                setForgoundLocationUpdate(journey.value)
                _syncJourney.value = true
            }
        }
    }

    fun removeDatabindService() {
        mBinder?.apply {
            _syncJourney.value = false
            setForgoundLocationUpdate(null)
        }

    }


    fun uploadJourneyToServer() {
        viewModelScope.launch(Dispatchers.Main) {
            if (null != journey.value) {

                repository.backupJourneyToServer(journey.value!!).collect({
                    when (it) {
                        is ApiResult.Success -> {
                            it.data
                        }

                        is ApiResult.Error<*> -> {
                            it.data
                        }

                        is ApiResult.Loading -> {
                            _syncJourney.value = it.data

                        }

                        else -> {

                        }
                    }

                })
            }
        }

    }

    fun deletePlaceAndItsImage(placeEntity: PlaceEntity) {
        viewModelScope.launch(Dispatchers.IO) {

            placeEntity?.listImage?.forEach {
                repository.deleteImage(it.id)
            }
            repository.deletePlace(placeEntity.id)

            _journey.value?.listPlaces?.find {
                it.id == placeEntity.id
            }?.also {
                _journey.value?.listPlaces?.remove(it)
                withContext(Dispatchers.Main)
                {
                    _journey.value = _journey.value
                }
            }
        }

    }


}