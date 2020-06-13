package com.attractions.wanm.services

import com.attractions.wanm.model_helper.pojo.Attraction
import com.attractions.wanm.model_helper.pojo.BackgroundNearbyAttraction

class Interface {

    interface Presenter{
        fun callbackSuccess(attraction: BackgroundNearbyAttraction.NearbyAttraction) //TODO: FIX POJO
        fun callbackError(t:Throwable)
        fun callbackErrorBackend(status:Int)
    }

    interface Model{
        fun getNearAttraction(latitude:Double,longitude:Double)
    }
}