package com.attractions.wanm.fragments.list

import android.util.Log
import com.attractions.wanm.model_helper.pojo.ResponseAttraction

class ListAttPresenter(private val view:Interface.View):Interface.Presenter {

    private val model:Interface.Model = ListAttModel(this)


    override fun requestAttractions(latitude:Double,longitude:Double) {
        this.model.getAttractions(latitude,longitude)
    }

    override fun callBackSuccess(responseAttraction: ResponseAttraction) {
        this.view.hideMainProgressBar()
        this.view.setDataIntoRecyclerView(responseAttraction.attractions)
    }

    override fun callBackError(t: Throwable) {
        this.view.showToastError()
        Log.e("ERROR LIST CALLBACK",t.localizedMessage!!)
    }


}