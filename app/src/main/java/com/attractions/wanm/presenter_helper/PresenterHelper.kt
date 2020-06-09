package com.attractions.wanm.presenter_helper

import kotlin.math.*

class PresenterHelper {

    companion object{
        fun calculateDistance(latitude1:Double,latitude2:Double,longitude1:Double,longitude2:Double):Double{
            val RADIUS_EARTH = 6371e3

            val latitude1RAD = latitude1*Math.PI/180
            val latitude2RAD = latitude2*Math.PI/180
            val diffLatRAD = latitude2RAD - latitude1RAD
            val diffLongRAD = (longitude2 - longitude1) *Math.PI/180

            val a = sin(diffLatRAD/2) * sin(diffLatRAD/2) +
                    +cos(latitude1RAD) * cos(latitude2RAD) * sin(diffLongRAD/2) * sin(diffLongRAD/2)
            val c = 2 * atan2(sqrt(a), sqrt(1-a))
            val d = (RADIUS_EARTH * c)
            return d
        }

        fun calculateDistanceInMeters(latitude1:Double,latitude2:Double,longitude1:Double,longitude2:Double):Int{
            return calculateDistance(latitude1,latitude2,longitude1,longitude2).toInt()
        }

        fun calculateDistanceInKilometers(latitude1:Double,latitude2:Double,longitude1:Double,longitude2:Double):Int{
            return (calculateDistance(latitude1,latitude2, longitude1, longitude2)/1000).toInt()
        }

        fun calculateDistanceInKilometersDouble(latitude1:Double,latitude2:Double,longitude1:Double,longitude2:Double):Double{
            return round(calculateDistance(latitude1,latitude2, longitude1, longitude2)/100)/10
        }
    }


}