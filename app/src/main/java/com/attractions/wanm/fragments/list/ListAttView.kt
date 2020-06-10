package com.attractions.wanm.fragments.list

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attractions.wanm.R
import com.attractions.wanm.model_helper.pojo.Attraction
import com.attractions.wanm.model_helper.pojo.ResponseAttraction
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class ListAttView: Fragment(),Interface.View {

    private val presenter:Interface.Presenter = ListAttPresenter(this)
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted:Boolean = false
    private var userLatLng:LatLng? = null

    companion object{
        private var instance:ListAttView? = null
        fun getInstance():ListAttView{
            if(instance == null) instance = ListAttView()
            return  instance as ListAttView
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_view,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLocationPermission()
//        if(userLatLng==null) {
//            val latitude = 59.946290
//            val longitude = 30.265529
//            this.presenter.requestAttractions(latitude, longitude)
//        }else this.presenter.requestAttractions(userLatLng!!.latitude, userLatLng!!.latitude)

    }

    override fun setDataIntoRecyclerView(attractions: List<Attraction>) {
        val view = view ?: return
        Log.d("COORD RE","${userLatLng!!.latitude},${userLatLng!!.longitude}")
        val adapter = RecyclerViewAdapterListOfAttraction(context!!,attractions,userLatLng)

        val recyclerView = view.findViewById<RecyclerView>(R.id.fragment_list_rv)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun showToastError() {
        Toast.makeText(context,"Произошла ошибка",Toast.LENGTH_SHORT).show()
    }

    override fun hideMainProgressBar() {
        val view = view?: return
        view.findViewById<ProgressBar>(R.id.fragment_list_progressBar).visibility= View.GONE
    }


    private fun saveLatLngOfUserPosition(){
        getDeviceLocationLatLng()
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("ASD","glp")
            mLocationPermissionGranted = true
            saveLatLngOfUserPosition()
        } else {
            Log.d("ASD","rp")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }


    private fun getDeviceLocationLatLng(){
        try {
            val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)
            if (mLocationPermissionGranted) {
                Log.d("ASD","gdl")
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener {
                    userLatLng = LatLng(it.latitude,it.longitude)
                    Log.d("COORd","${it.latitude},${it.longitude}")
                    this.presenter.requestAttractions(it.latitude, it.longitude)
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                    saveLatLngOfUserPosition()
                    this.presenter.requestAttractions(userLatLng!!.latitude, userLatLng!!.longitude)
                }else{
                    val latitude = 59.946290
                    val longitude = 30.265529
                    this.presenter.requestAttractions(latitude, longitude)
                }
            }
        }
    }


}