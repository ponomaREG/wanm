package com.attractions.wanm.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.attractions.wanm.R
import com.google.android.gms.location.*


class LocationService:Service() {

    private val CHANNEL_ID:String = "location"
    private val NOTIFY_ID = 1


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("CREATE","CREATEAR")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("STRAT","CREATEAR")
        if(checkPermissions()) {
            LocationHelper().start()
        }else{
            Log.d("ERROR","PERAERA")
            stopSelf()
        }
        for(a in 1..100){
            Log.d("A ",a.toString())

        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun checkPermissions():Boolean{
        return ((ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                )
    }

    override fun onDestroy() {
        Log.d("DESTIOR","ASDASD")
        super.onDestroy()
    }


    private fun showNotification(latitude:Double,longitude:Double){
        val notify = NotificationCompat.Builder(this,CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Bruh")
            .setContentText("${latitude},${longitude}")
            .setSmallIcon(R.drawable.map_icon_default)
            .build()
        NotificationManagerCompat.from(this).notify(NOTIFY_ID,notify)
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    inner class LocationHelper{

        private val INTERVAL = 10000L
        private val FASTEST_INTERVAL = 10000L

        @SuppressLint("RestrictedApi")
        fun start(){
            val locationRequest:LocationRequest = LocationRequest()
            locationRequest.interval = INTERVAL
            locationRequest.fastestInterval = FASTEST_INTERVAL
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            val locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val currentLocation: Location = locationResult.getLastLocation()
                    createNotificationChannel()
                    showNotification(currentLocation.latitude,currentLocation.longitude)
                }
            }

            val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@LocationService)
            mFusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }



    }





}