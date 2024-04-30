package com.inter.mylocation

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority


object LocationRepository {

    private val _myLocation: MutableLiveData<Location> = MutableLiveData()
    val myLocation: LiveData<Location> = _myLocation



    private val LOCATION_UPDATE_INTERVAL = 10L
    private val callback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            p0?.lastLocation?.apply {
                _myLocation.value = this
            }

            Log.d(
                "MYLOCATIONMY",
                "" + p0.lastLocation?.latitude + " : " + p0.lastLocation?.longitude
            )
        }

    }


    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private fun getLocationRequest(): LocationRequest {
        val locationBuilder = LocationRequest.Builder(1000 * LOCATION_UPDATE_INTERVAL)
        locationBuilder.setMinUpdateIntervalMillis(1000 * 30)
        locationBuilder.setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        val locationRequest: LocationRequest = locationBuilder.build()
        return locationRequest
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(application: Application) {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(application.applicationContext)

        fusedLocationProviderClient?.requestLocationUpdates(
            getLocationRequest(), callback, Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient?.removeLocationUpdates(callback)
    }
}