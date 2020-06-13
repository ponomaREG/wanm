package com.attractions.wanm.services

import com.attractions.wanm.model_helper.pojo.Attraction

class Interface {

    interface Presenter{
        fun callBackShowNotifyWithNearAttraction(attraction: Attraction)
    }

    interface Model{
        fun getNearAttraction(latitude:Double,longitude:Double)
    }
}