package com.example.mylocation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mylocation.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var currentLocacation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode =101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        getCurrentLocationUse()
    }

    private fun getCurrentLocationUse() {
        if(ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=
            PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),permissionCode)
            return
        }

       val getLocation= fusedLocationProviderClient.lastLocation.addOnSuccessListener {
           location ->

           if(location != null){

               currentLocacation =location

               Toast.makeText(applicationContext,currentLocacation.latitude.toString() + "" +
               currentLocacation.longitude.toString(), Toast.LENGTH_LONG).show()

               val mapFragment = supportFragmentManager
                   .findFragmentById(R.id.map) as SupportMapFragment
               mapFragment.getMapAsync(this)




           }
       }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){

            permissionCode -> if(grantResults.isNotEmpty() && grantResults[0]==
                    PackageManager.PERMISSION_GRANTED){

                getCurrentLocationUse()
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
       val latLng =LatLng(currentLocacation.latitude ,currentLocacation.longitude)
        val markerOptions= MarkerOptions().position(latLng).title("Current Location")

        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,7f))
        googleMap?.addMarker(markerOptions)
    }
}