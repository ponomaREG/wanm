package com.attractions.wanm.fragments.list

import com.attractions.wanm.model_helper.pojo.Attraction
import com.attractions.wanm.model_helper.pojo.ResponseAttraction

interface Interface {

    interface View{

        fun setDataIntoRecyclerView(attractions: List<Attraction>)
        fun showToastError()
        fun hideMainProgressBar()
    }

    interface Presenter{

        fun requestAttractions(latitude:Double,longitude:Double)
        fun callBackSuccess(responseAttraction: ResponseAttraction)
        fun callBackError(t:Throwable)

    }

    interface Model{
        fun getAttractions(latitude:Double,longitude:Double)
    }
}