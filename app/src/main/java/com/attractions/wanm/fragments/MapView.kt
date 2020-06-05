package com.attractions.wanm.fragments


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.attractions.wanm.R
import com.attractions.wanm.main.MainActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng


class MapView : Fragment(), OnMapReadyCallback {

    private var mLocationPermissionGranted:Boolean = false
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    companion object{
        private var instance:MapView? = null

        fun getInstance():MapView{
            if(instance == null){
                instance = MapView()
            }
            return instance as MapView
        }
    }




    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_view,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("LOG","ONMAPREADY")
        updateLocation()
    }


    private fun getLocationPermission() { /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("ASADASD","ASDASDAS")
            mLocationPermissionGranted = true
            updateLocation()

        } else {
            Log.d("ASDASDAS","!@#!@#!@")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
//            ActivityCompat.requestPermissions(
//                activity as MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
//            )
        }
    }


    private fun getDeviceLocation() { /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)
            if (mLocationPermissionGranted) {
                Log.d("LOG","mLocationPermissionGranted get")
               mFusedLocationProviderClient.getLastLocation().addOnSuccessListener {
                   val latLng = LatLng(it.latitude,it.longitude)
                   Log.d("LOG","mLocationPermissionGranted move")
                   mMap.moveCamera(
                       CameraUpdateFactory.newLatLngZoom(latLng,13F)
                   )
               }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    private fun updateLocation(){
        try {
            if (mLocationPermissionGranted) {
                Log.d("LOG","mLocationPermissionGranted")
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
                getDeviceLocation()
            } else {
                Log.d("LOG ","NONE")
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("REQUEST CODE", requestCode.toString())
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                    updateLocation()
                }
            }
        }

    }
}
