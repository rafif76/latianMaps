package com.example.mapsforpatest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mapsforpatest.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() , OnMapReadyCallback {


    private lateinit var mMap : GoogleMap

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)



    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val jakarta = LatLng(-6.175499061310101, 106.82761413755082)
        mMap.addMarker(MarkerOptions().position(jakarta).title("Marker di jakarta, Monas"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jakarta))


    }


}