package com.attractions.wanm.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.attractions.wanm.R
import com.attractions.wanm.main.MainActivity
import com.attractions.wanm.model_helper.pojo.Attraction
import com.attractions.wanm.model_helper.pojo.BackgroundNearbyAttraction
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*


class LocationService:Service(), LocationListener,Interface.Presenter {

    private val ACTION_STOP_SERVICE = "stopThis"
    private val TAG = "BackgroundLocation"
    private val TAG_LOCATION = "TAG_LOCATION"
    private val CHANNEL_ID = "channel_location"
    private val CHANNEL_NAME = "channel_location"

    private var context: Context? = null

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocationRequest: LocationRequest? = null

    private val model:Interface.Model = LocationServiceModel(this)


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e("onStartCommand","1")
        //Проверяем получили действие с отменой данного сервиса
        if(ACTION_STOP_SERVICE == intent!!.action){
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(101)
            stopSelf()
        }

        //Показываем уведомление пользователю о его отслеживании местоположения
        showForegroundNotification()
        //Получаем доступ к апи гугла
        buildGoogleApiClient()
        return START_STICKY
    }


    private fun showForegroundNotification() {

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        var builder: NotificationCompat.Builder? = null

        //Интент для остановки
        val stopSelf = Intent(this, LocationService::class.java)
        //Действие , которое мы сверяем в начале
        stopSelf.action = ACTION_STOP_SERVICE
        //Действие для актион кнопки
        val pendingIntentStopperThis = PendingIntent.getService(this,0,stopSelf,PendingIntent.FLAG_CANCEL_CURRENT)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Создаем канал , если позволяет версия
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(channel)
            //Получаем строителя уведомления
            builder =
                NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            builder.setChannelId(CHANNEL_ID)
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
        } else {
            // Получаем строителя уведмолений если версия не позволяет создание канала уведомлений
            builder =
                NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        }
        val notificationSound: Uri =
            RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION)
        builder.apply {
            setContentTitle("Wanm")
            setContentText("Сейчас мы сканируем достопримечательности рядом")
            setSound(notificationSound)
            setAutoCancel(true)
            setSmallIcon(R.drawable.map_icon_default)
            setContentIntent(pendingIntent)
            addAction(R.drawable.map_desc_placeholder,"Прекратить",pendingIntentStopperThis)
        }
        val notification = builder.build()
        startForeground(101, notification)
    }


    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }


    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude.toString()
        val longitude = location.longitude.toString()

        if (latitude.equals("0.0", ignoreCase = true) && longitude.equals(
                "0.0",
                ignoreCase = true
            )
        ) {
            requestLocationUpdate()//Выполняем еще запрос, так как на прошлый гугл выдал нулл
        }else{
            //TODO:REQUEST
            Log.e("COORD","${latitude},${longitude}")
            model.getNearAttraction(latitude.toDouble(),longitude.toDouble())
        }
    }


    private fun buildGoogleApiClient() {
        //Получаем провайдер
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        //Настройки
        val mSettingsClient = LocationServices.getSettingsClient(context!!)
        //Апи клиент гугла
        val mGoogleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(object:GoogleApiClient.ConnectionCallbacks{ //Слушатель успешного соединения
                @SuppressLint("RestrictedApi")
                override fun onConnected(p0: Bundle?) {
                    mLocationRequest = LocationRequest() //Запрос на получение локации
                    mLocationRequest!!.interval = 30 * 1000.toLong()
                    mLocationRequest!!.fastestInterval = 20 * 1000.toLong()
                    mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val builder = LocationSettingsRequest.Builder()
                    builder.addLocationRequest(mLocationRequest!!)//Настройки
                    builder.setAlwaysShow(true)
                    val mLocationSettingsRequest = builder.build()
                    mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
                        .addOnSuccessListener {
                            requestLocationUpdate()//Успешное подключение для выполнение запросов
                        }.addOnFailureListener { e ->
                            when ((e as ApiException).statusCode) {
                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                    val REQUEST_CHECK_SETTINGS = 214
                                    val rae = e as ResolvableApiException
                                    rae.startResolutionForResult(
                                        context as AppCompatActivity?,
                                        REQUEST_CHECK_SETTINGS
                                    )
                                } catch (sie: SendIntentException) {
                                    Log.e(TAG_LOCATION, "Unable to execute request.")
                                }
                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.e(
                                    TAG_LOCATION,
                                    "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                                )
                            }
                        }
                }

                override fun onConnectionSuspended(p0: Int) {
                }

            })
            .addOnConnectionFailedListener { buildGoogleApiClient() }//Выполняем снова
            .addApi(LocationServices.API)
            .build()
        val googleAPI = GoogleApiAvailability.getInstance()
        val resultCode = googleAPI.isGooglePlayServicesAvailable(context)//Есть ли подключение к АПИ гугла
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient!!.connect()
        }
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.e(TAG_LOCATION, "Location Received")
                onLocationChanged(locationResult.lastLocation)//Выводим полученную локацию , если мы ее получили
            }
        }
    }

    override fun onDestroy() {
        Log.e(TAG, "Service Stopped")
        if (mFusedLocationClient != null) {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            Log.e(TAG_LOCATION, "Location Update Callback Removed")
        }
        super.onDestroy()
    }


    override fun callbackSuccess(attraction: BackgroundNearbyAttraction.NearbyAttraction) {
        val intentShower = Intent(context,MainActivity::class.java)
        intentShower.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intentShower.putExtra("id",attraction.id)
        val pendingIntent = PendingIntent.getActivity(context,0,intentShower,0)
        val notification:Notification = NotificationCompat.Builder(applicationContext,CHANNEL_ID)
            .setContentTitle(attraction.title)
            .setContentText("Расстояние: ${attraction.distance}")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.map_icon_museum)
            .build()

        NotificationManagerCompat.from(context!!).notify(1,notification)
    }

    override fun callbackError(t: Throwable) {
            Log.e("CallbackNearby",t.localizedMessage!!)
    }

    override fun callbackErrorBackend(status: Int) {
        Log.e("CallbackNearbyBackend","Status code: $status")
    }


}