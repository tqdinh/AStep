package com.inter.mylocation


import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.inter.notification.MyNotification

class ForegroundLocation : LifecycleService() {

    private companion object {
        const val NOTIFICATION_ID = 2
    }

    val ACTION_STOP_UPDATES = "ACTION_STOP_UPDATES"
    var stopIntent: PendingIntent? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent?.action == ACTION_STOP_UPDATES) {
            LocationRepository.stopLocationUpdates()
            stopSelf()
        } else {
            try {

                stopIntent = PendingIntent.getService(
                    application.applicationContext,
                    0,
                    Intent(this, this::class.java).setAction(ACTION_STOP_UPDATES),
                    PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val myNotification = MyNotification(application.applicationContext)
                myNotification.createNotificationChannel()
                LocationRepository.startLocationUpdates(application)
                val notificationBuilder = myNotification.createNotificationBuilder()
                notificationBuilder.setContentIntent(stopIntent)
                notificationBuilder.setOngoing(true)
//                notificationBuilder.setAutoCancel(false)
                notificationBuilder.addAction(android.R.drawable.ic_menu_close_clear_cancel,"Stop",stopIntent)
                startForeground(1, notificationBuilder.build())

            } catch (e: Exception) {
                e.toString()
            }
        }



        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
        startService(Intent(this, this::class.java))
    }


}

