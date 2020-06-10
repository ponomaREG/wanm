package com.attractions.wanm.fragments.map


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.attractions.wanm.R
import com.attractions.wanm.fragments.descAttraction.BsvDescView
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapView : Fragment(), OnMapReadyCallback, Interface.View {

    private var mLocationPermissionGranted:Boolean = false
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
//    private val markers:List<Marker> = ArrayList<Marker>()

    private val presenter:Interface.Presenter = MapViewPresenter(this)

    companion object{
        private var instance: MapView? = null
        private var customLatLng:LatLng? = null

        fun getInstance(): MapView {
            if(instance == null){
                instance =
                    MapView()
            }
            return instance as MapView
        }

        fun getInstance(latitude: Double,longitude: Double):MapView{
            if(instance == null){
                instance =
                    MapView()
            }
            customLatLng = LatLng(latitude,longitude)
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

        setMarksOnMap()
        updateLocation()

    }


    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
            updateLocation()
            setMarksOnMap()

        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }


    private fun getDeviceLocationAndMoveOn() {
        val latLngCurrent = getDeviceLocationLatLng()
        if(latLngCurrent != null) moveOnLatLng(latLngCurrent)

    }

    private fun getDeviceLocationLatLng():LatLng?{
        var latLng:LatLng? = null
        try {
            val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener {
                    latLng = LatLng(it.latitude,it.longitude)
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
        return latLng
    }

    private fun moveOnLatLng(latLng: LatLng){
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(latLng,11F)
        )
    }

    private fun updateLocation(){
        try {
            if (mLocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
                if(customLatLng != null) moveOnLatLng(customLatLng!!)
                else getDeviceLocationAndMoveOn()
            } else {
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
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                    updateLocation()
                }else {
                    setMarksOnMap()
                    moveOnLatLng(LatLng(59.946290,30.265529))
                }
            }
        }

    }




    private fun initOclToInfoWindow(){
        mMap.setOnInfoWindowClickListener {
            val id:Int = it.tag as Int
            Log.d("id",id.toString())
            BsvDescView.getInstance(id).show(fragmentManager!!,"Description of mark")
        }
    }




    private fun setMarksOnMap(){
        presenter.requestMarks()
    }


    private fun resizeMapIcons(iconName: String?, width: Int, height: Int): Bitmap? {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(iconName, "drawable", activity!!.packageName)
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

    override fun addMark(latitude: Double,
                         longitude: Double,
                         title: String,
                         snippet: String,
                         type:String,
                         id:Int) {
        val latIng = LatLng(latitude, longitude)
        val markerOptions = MarkerOptions()
            .position(latIng)
            .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(type,100,100)))
            .title(title)
            .snippet(snippet)
        val newMarker = mMap.addMarker(markerOptions)
        if(customLatLng != null) {
            if (customLatLng!!.latitude == latitude
                && customLatLng!!.longitude == longitude
            ) newMarker.showInfoWindow()
        }
        newMarker.tag = id
        initOclToInfoWindow()
    }


}
