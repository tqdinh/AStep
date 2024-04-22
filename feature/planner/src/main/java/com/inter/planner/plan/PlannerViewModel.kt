package com.inter.planner.plan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inter.planner.entity.JourneyEntity
import com.inter.planner.repositories.JourneyRepository
import com.inter.planner.repositories.JourneyRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(val repository: JourneyRepository) : ViewModel() {

    val listJourney: LiveData<List<JourneyEntity>> =
        (repository as JourneyRepositoryImpl).listJourney



    fun getJourney() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListJourney()
        }
    }

    fun migrate() {
        viewModelScope.launch(Dispatchers.IO) {
            //repository.migrateImages()
        }
    }

    fun getPlaceOfJourney(journeyId: String) {



    }
}