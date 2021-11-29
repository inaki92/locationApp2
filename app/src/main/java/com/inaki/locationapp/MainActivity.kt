package com.inaki.locationapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.inaki.locationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermission(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_CODE
        )

        Notification.createNotificationChannel(baseContext)
    }

    override fun onResume() {
        super.onResume()

        binding.trackLocation.setOnClickListener {
            startLocationService()
        }

        binding.stopTracking.setOnClickListener {
            LocationService.stopService(applicationContext)
        }

        binding.showMap.setOnClickListener {
            if (LocationService.isRunning) {
                Intent(baseContext, MapsActivity::class.java).apply {
                    startActivity(this)
                }
            } else {
                Toast.makeText(baseContext, "Please start tracking location", Toast.LENGTH_LONG).show()
            }
        }
    }

    // This function is called when the user accepts or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when the user is prompt for permission.
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE) {
            grantResults.forEach {
                if (grantResults.isNotEmpty() && it == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(baseContext, "Permissions granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "No permissions granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startLocationService() {
        LocationService.startService(
            applicationContext,
            Notification.createNotification(applicationContext)
        )
    }

    // Function to check and request permission.
    private fun checkPermission(permissions: Array<String>, requestCode: Int) {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_DENIED) {
                // Requesting the permission
                ActivityCompat.requestPermissions(this, permissions, requestCode)
            } else {
                Toast.makeText(baseContext, "Permission already granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 101
    }

    override fun onMapReady(map: GoogleMap) {
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}