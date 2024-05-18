package com.inter.mylocation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.inter.entity.planner.JourneyEntity
import com.inter.entity.planner.PlaceEntity
import com.inter.planner.repositories.JourneyRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundLocationService : Service() {

    @Inject
    lateinit var repository: JourneyRepository
    var myJourney: JourneyEntity? = null

    lateinit var notificationBuilder: NotificationCompat.Builder
    private val binder: IBinder = BackgroundLocationBindder()

    val ACTION_STOP_UPDATES = "ACTION_STOP_UPDATES"

    lateinit var callback: LocationCallback
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationBuilder: LocationRequest.Builder

    private companion object {
        const val NOTIFICATION_ID = 2
        val CHANNEL_ID = "ASTEP_CHANNEL_ID"
        val CHANNEL_NAME = "ASTEP_CHANNLE"
        private val LOCATION_UPDATE_INTERVAL = 10L
    }

    inner class BackgroundLocationBindder : Binder() {
        fun getJourney() = myJourney

        @SuppressLint("MissingPermission")
        fun setForgoundLocationUpdate(journey: JourneyEntity?) {
            myJourney = journey

            if (null != journey) {
                notificationBuilder.setContentText("Start tracking")
                with(NotificationManagerCompat.from(applicationContext)) {
                    notify(NOTIFICATION_ID, notificationBuilder.build())
                }

                startForeground(NOTIFICATION_ID, notificationBuilder.build())


                fusedLocationProviderClient.requestLocationUpdates(
                    locationBuilder.build(), callback, Looper.getMainLooper()
                )


            } else {
                notificationBuilder.setContentText("No tracking")
                with(NotificationManagerCompat.from(applicationContext)) {
                    notify(NOTIFICATION_ID, notificationBuilder.build())
                }

                this@BackgroundLocationService.stopForeground(STOP_FOREGROUND_REMOVE)
            }


        }
    }


    override fun onCreate() {
        super.onCreate()
        callback = object : LocationCallback() {
            @SuppressLint("MissingPermission")
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
//
//                notificationBuilder.setContentText("Tracking ${p0.lastLocation?.latitude} - ${p0.lastLocation?.longitude}")
//                with(NotificationManagerCompat.from(applicationContext)) {
//                    notify(NOTIFICATION_ID, notificationBuilder.build())
//                }
                if (null != myJourney) {
                    p0.lastLocation?.apply {
                        val placeEntity = PlaceEntity(
                            id = UUID.randomUUID().toString(),
                            ref_journey_id = myJourney!!.id,
                            timestamp = System.currentTimeMillis(),
                            title = "------",
                            desc = "desccc",
                            lat = this.latitude,
                            lon = this.longitude
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            repository.createPlace(placeEntity)
                        }
                    }
                }
            }
        }

        locationBuilder = LocationRequest.Builder(1000 * LOCATION_UPDATE_INTERVAL)
            .setMinUpdateIntervalMillis(1000 * 30)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            (application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                serviceChannel
            )
        }

        val stopIntent = PendingIntent.getService(
            application.applicationContext,
            0,
            Intent(this@BackgroundLocationService, this::class.java).setAction(
                ACTION_STOP_UPDATES
            ),
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        //create notification builder
        notificationBuilder =
            NotificationCompat.Builder(application.applicationContext, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setContentTitle("Astep")
                .setContentText("Location in use")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationBuilder.setContentIntent(stopIntent)
        notificationBuilder.setOngoing(true)
//                notificationBuilder.setAutoCancel(false)
        notificationBuilder.addAction(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Stop",
            stopIntent
        )

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent?.action == ACTION_STOP_UPDATES) {

            if (null != myJourney)
                stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }


        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(callback)
    }

}