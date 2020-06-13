package com.attractions.wanm.services

import com.attractions.wanm.model_helper.Network
import com.attractions.wanm.model_helper.pojo.BackgroundNearbyAttraction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class LocationServiceModel(private val presenter:Interface.Presenter):Interface.Model {



    interface callbackFromModel{ //TODO:FIX POJO
        fun callbackSuccess(attraction: BackgroundNearbyAttraction.NearbyAttraction)
        fun callbackError(t:Throwable)
        fun callbackErrorBackend(status:Int)
    }

    override fun getNearAttraction(latitude:Double,longitude:Double) {
        Network.getInstance().getReposAttractions().getResponseAttractionNearByUserBackground(latitude, longitude)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if(it.status == 0){

                }
            },{

            })
    }


}