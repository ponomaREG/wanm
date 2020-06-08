package com.attractions.wanm.fragments.map

import android.util.Log
import com.attractions.wanm.model_helper.pojo.ResponseAttraction
import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

class MapViewPresenter(val view:Interface.View) : Interface.Presenter {

    private val model:Interface.Model = MapViewModel(this)


    override fun requestMarks() {
        model.getAttractions()
    }


    override fun callbackSuccess(responseAttraction: ResponseAttraction) {
       val latLng = LatLng(59.9460943,30.2659576)
       for (attraction in responseAttraction.attractions){
           this.view.addMark(attraction.latitude,
               attraction.longitude,
               attraction.title,
               attraction.snippet,
               getStringNaMeOfIconMapByTypeId(attraction.type),
               attraction.id)

       }
    }

    override fun callbackError(t: Throwable) {
        Log.e("ERROR",t.localizedMessage!!)
    }


    private fun getStringNaMeOfIconMapByTypeId(id:Int):String{
        return when(id){
            0 -> "map_icon_museum"
            2 -> "map_icon_church"
            else -> "map_icon_default"
        }
    }


    private fun calculateDistance(latitude1:Double,latitude2:Double,longitude1:Double,longitude2:Double):Int{
        val RADIUS_EARTH = 6371e3

        val latitude1RAD = latitude1*Math.PI/180
        val latitude2RAD = latitude2*Math.PI/180
        val diffLatRAD = latitude2RAD - latitude1RAD
        val diffLongRAD = (longitude2 - longitude1) *Math.PI/180

        val a = sin(diffLatRAD/2) * sin(diffLatRAD/2) +
                +cos(latitude1RAD) * cos(latitude2RAD) * sin(diffLongRAD/2) * sin(diffLongRAD/2)
        val c = 2 * atan2(sqrt(a), sqrt(1-a))
        return (RADIUS_EARTH * c).toInt()
    }
}