package com.inter.mylocation


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.inter.notification.MyNotification

class ForegroundLocation : LifecycleService() {

    private companion object {
        const val NOTIFICATION_ID = 2
        val CHANNEL_ID = "ASTEP_CHANNEL_ID"
        val CHANNEL_NAME = "ASTEP_CHANNLE"
    }

    val ACTION_STOP_UPDATES = "ACTION_STOP_UPDATES"
    var stopIntent: PendingIntent? = null

    override fun onCreate() {
        super.onCreate()
        try {
            stopIntent = PendingIntent.getService(
                application.applicationContext,
                0,
                Intent(this, this::class.java).setAction(ACTION_STOP_UPDATES),
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )


            // create channel
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
            //create notification builder
            val notificationBuilder =
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

            startForeground(NOTIFICATION_ID, notificationBuilder.build())
            LocationRepository.startLocationUpdates(application.applicationContext)
            LocationRepository.myLocation.observe(this, object : Observer<Location> {
                override fun onChanged(value: Location) {
                    value.longitude + value.latitude

                    Log.d("----LOCATION_CAHNGE---", "${value.longitude} ; ${value.latitude}")


                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }

//                    notificationBuilder.setContentText("${value.longitude} ; ${value.latitude}")
//                    with(NotificationManagerCompat.from(applicationContext)) {
//                        notify(NOTIFICATION_ID, notificationBuilder.build())
//                    }


                }

            })

        } catch (e: Exception) {
            e.toString()
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent?.action == ACTION_STOP_UPDATES) {
            LocationRepository.stopLocationUpdates()
            stopSelf()
        }



        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
        startService(Intent(this, this::class.java))
    }


    fun stopService() {
        stopSelf()
    }

}

