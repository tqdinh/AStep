package com.example.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inter.entity.planner.JourneyEntity
import com.inter.planner.repositories.JourneyRepository
import com.inter.planner.repositories.JourneyRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repository: JourneyRepository) : ViewModel() {

    val listJourney: LiveData<List<JourneyEntity>> =
        (repository as JourneyRepositoryImpl).listJourney

    val listnumber: LiveData<List<Int>> = (repository as JourneyRepositoryImpl).listnumber

    fun getJourney() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListJourney()
        }
    }


    fun migrate() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.migrateImages()
        }
    }

    fun getPlaceOfJourney(journeyId: String) {

    }
}


