package com.example.home.camera

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.inter.entity.planner.ImageEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.mylocation.LocationRepository
import com.inter.planner.repositories.JourneyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(val repository: JourneyRepository) : ViewModel() {

    val myLocation: LiveData<Location> = LocationRepository.myLocation


    fun createPlace(journeyId: String, imagePath: String) {

        LocationRepository.getCurrentLocation()

        var placeEntity: PlaceEntity? = null
        myLocation.value?.apply {

            placeEntity = PlaceEntity(
                id = UUID.randomUUID().toString(),
                ref_journey_id = journeyId,
                timestamp = System.currentTimeMillis(),
                title = "------",
                desc = "desccc",
                lat = this.latitude,
                lon = this.longitude
            )
        }

        placeEntity?.apply {
            viewModelScope.launch(Dispatchers.IO) {

                val placeId = async { repository.createPlace(this@apply).id }.await()

                async {
                    val imageEntity = ImageEntity(
                        id = UUID.randomUUID().toString(),
                        ref_place_id = placeId,
                        path = imagePath,
                        url = ""
                    )
                    repository.createImage(imageEntity)
                }.await()


            }
        }


    }

    fun createImageOnPlace() {

    }
}