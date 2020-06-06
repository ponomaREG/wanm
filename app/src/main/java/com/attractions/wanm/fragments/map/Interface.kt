package com.attractions.wanm.fragments.map

import com.attractions.wanm.model_helper.pojo.ResponseAttraction

interface Interface {


    interface View{
        fun addMark(latitude:Double,longitude:Double,title:String,snippet:String)
    }

    interface Presenter{
        fun requestMarks()
        fun callbackSuccess(responseAttraction: ResponseAttraction)
        fun callbackError(t:Throwable)
    }

    interface Model{
        fun getAttractions()
    }
}