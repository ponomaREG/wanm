package com.attractions.wanm.fragments.map

import com.attractions.wanm.model_helper.Network
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MapViewModel(val presenter: Interface.Presenter):Interface.Model {



    override fun getAttractions() {
        Network.getInstance().getReposAttractions().getResponseAttraction()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    this.presenter.callbackSuccess(it)
                },
                {
                    this.presenter.callbackError(it)
                }
            )

    }
}