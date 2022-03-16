package com.example.mapsforpatest

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mapsforpatest.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() , OnMapReadyCallback {




    private lateinit var mMap : GoogleMap

    private lateinit var binding: ActivityMainBinding

    private var latitude :  Double = 0.toDouble()
    private var longitude :  Double = 0.toDouble()


    private lateinit var mlastLocation : Location

    private var mMarker : Marker? = null

    //location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object {
        private const val MY_PERMISSION_CODE : Int = 1000
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        //Request runtime permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkLocationPermission()
        buildLocationRequest()
        buildLocationCallback()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
            Looper.myLooper()!!
        )

    }

    private fun buildLocationRequest() {

        locationRequest = LocationRequest.create().apply {

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
           fastestInterval = 3000
            smallestDisplacement = 10f

        }


    }

    private fun buildLocationCallback() {
        locationCallback = object  : LocationCallback (){
            override fun onLocationResult(p0: LocationResult) {
                mlastLocation = p0!!.locations.get(p0!!.locations.size-1) // means get Last Locations.

                if (mMarker != null)
                {
                    mMarker!!.remove()
                }
                latitude = mlastLocation.latitude
                longitude = mlastLocation.longitude

                val latLng = LatLng (latitude,longitude)
                val markerOptions= MarkerOptions().position(latLng)
                    .title("our position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                mMarker = mMap!!.addMarker(markerOptions)

                //move Camera
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))

            }

        }

    }

    private fun checkLocationPermission() : Boolean {

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            else
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)

            return false

        }

        else return true

    }




    //override onRequestPermissionResult

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode) {
        MY_PERMISSION_CODE -> {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                )
                    if (checkLocationPermission()) {
                        mMap!!.isMyLocationEnabled = true
                    }
            } else {
                Toast.makeText(this, "Permission Denied, Toast", Toast.LENGTH_SHORT).show()
            }

        }
    }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }



    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

      /*  val jakarta = LatLng(-6.175499061310101, 106.82761413755082)
        mMap.addMarker(MarkerOptions().position(jakarta).title("Marker di jakarta, Monas"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta))

       */


        //init Google play service

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap!!.isMyLocationEnabled = true

            }
        }
        else   mMap!!.isMyLocationEnabled = true

        //Enable Zoom Control
        mMap.uiSettings.isZoomControlsEnabled=true


    }


}