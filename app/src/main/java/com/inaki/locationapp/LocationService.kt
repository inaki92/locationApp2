package com.inaki.locationapp

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

class LocationService : Service() {

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    private var locationRequest: LocationRequest? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        Log.d("LocationService", "service created")
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // super.onStartCommand(intent, flags, startId)
        Log.d("LocationService", "service started")
        intent?.let { mIntent ->
            Log.d("LocationService", (mIntent.getParcelableExtra<Notification>(NOTIFICATION_KEY)).toString())

            locationRequest?.let {
                Log.d("LocationService", "REQUEST LOCATION")
                fusedLocationProviderClient.requestLocationUpdates(it, getPendingIntent())
            } ?: run {
                locationRequest = LocationRequest.create()
                    .setInterval(TimeUnit.SECONDS.toMillis(30))
                    .setFastestInterval(TimeUnit.SECONDS.toMillis(30))
                    .setPriority(PRIORITY_HIGH_ACCURACY)
                    .apply {
                        Log.d("LocationService", "REQUEST LOCATION")
                        Log.d("LocationService", this.toString())
                        fusedLocationProviderClient.requestLocationUpdates(this, getPendingIntent())
                    }
            }

            startForeground(2, mIntent.getParcelableExtra(NOTIFICATION_KEY))
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        Log.d("LocationService", "service stopped")
        fusedLocationProviderClient.removeLocationUpdates(getPendingIntent())
    }

    private fun getPendingIntent(): PendingIntent {
        Intent(baseContext, LocationReceiver::class.java).apply {
            return PendingIntent.getBroadcast(baseContext, 2, this, PendingIntent.FLAG_CANCEL_CURRENT)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        var isRunning = false
        const val NOTIFICATION_KEY = "NOTIFICATION"

        fun startService(context: Context, notification: Notification) {
            Intent(context, LocationService::class.java).apply {
                putExtra(NOTIFICATION_KEY, notification)
                context.startService(this)
            }
        }

        fun stopService(context: Context) {
            Intent(context, LocationService::class.java).apply {
                context.stopService(this)
            }
        }
    }
}