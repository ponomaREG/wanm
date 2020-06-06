package com.attractions.wanm.fragments.map

import android.util.Log
import com.attractions.wanm.model_helper.pojo.ResponseAttraction

class MapViewPresenter(val view:Interface.View) : Interface.Presenter {

    private val model:Interface.Model = MapViewModel(this)


    override fun requestMarks() {
        model.getAttractions()
    }


    override fun callbackSuccess(responseAttraction: ResponseAttraction) {
       for (attraction in responseAttraction.attractions){
           Log.d("ATTRACTION",attraction.title)
           this.view.addMark(attraction.longitude,
               attraction.latitude,
               attraction.title,
               attraction.snippet,
               getStringNaMeOfIconMapByTypeId(attraction.type))
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
}