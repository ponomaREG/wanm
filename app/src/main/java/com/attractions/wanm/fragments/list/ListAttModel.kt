package com.attractions.wanm.fragments.list

import com.attractions.wanm.model_helper.Network
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListAttModel(private val presenter:Interface.Presenter):Interface.Model {




    override fun getAttractions(latitude:Double,longitude:Double) {
        Network.getInstance().getReposAttractions()
            .getResponseAttractionNearByUser(latitude,longitude)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                this.presenter.callBackSuccess(it)
            },{
                this.presenter.callBackError(it)
            })
    }


}