package com.inaki.locationapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationResult

class LocationReceiver : BroadcastReceiver() {

    private val coordinate: MyCoordinate by lazy {
        MyCoordinate.getInstance()
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("LocationService", "location received")
        if (LocationResult.hasResult(intent)) {
            Log.d("LocationService", "intent has result")
            val locationResult = LocationResult.extractResult(intent)
            val location: Location? = locationResult.lastLocation
            location?.let {
                coordinate.latitude = location.latitude
                coordinate.longitude = location.longitude
                Log.d("LocationService", "latitude: ${location.latitude}, longitude: ${location.longitude}")
            } ?: Log.e(LocationReceiver::class.java.simpleName, "*** location object is null ***")
        }
    }
}