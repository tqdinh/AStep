package com.inter.planner.journey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inter.entity.planner.ApiResult
import com.inter.entity.planner.JourneyEntity
import com.inter.planner.repositories.JourneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class JourneyViewModel @Inject constructor(val repository: JourneyRepository) : ViewModel() {


    val _journey: MutableLiveData<com.inter.entity.planner.JourneyEntity> = MutableLiveData()
    val journey: LiveData<com.inter.entity.planner.JourneyEntity> = _journey

    val _syncJourney: MutableLiveData<Boolean> = MutableLiveData()
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
}