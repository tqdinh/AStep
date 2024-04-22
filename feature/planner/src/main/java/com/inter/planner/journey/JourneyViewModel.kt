package com.inter.planner.journey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inter.planner.entity.JourneyEntity
import com.inter.planner.repositories.JourneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JourneyViewModel @Inject constructor(val repository: JourneyRepository) : ViewModel() {


    val _journey: MutableLiveData<JourneyEntity> = MutableLiveData()
    val journey: LiveData<JourneyEntity> = _journey

    fun getJourney(journeyId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            repository.getJourney(journeyId).collect()
            {
                _journey.value = it
            }
        }

    }
}