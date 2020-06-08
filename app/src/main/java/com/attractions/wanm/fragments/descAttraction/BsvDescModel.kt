package com.attractions.wanm.fragments.descAttraction

import com.attractions.wanm.model_helper.Network
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BsvDescModel(private val presenter:Interface.Presenter):Interface.Model {



    override fun getData(id:Int) {
        Network.getInstance().getReposAttractions().getResponseAttraction(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if(it.status == 0) this.presenter.callBackSuccess(it.attractions[0])
                else this.presenter.callBackErrorBackend(it.status)
            },{
                this.presenter.callBackError(it)
            })
    }
}