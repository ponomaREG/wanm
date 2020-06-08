package com.attractions.wanm.fragments.descAttraction

import android.util.Log
import com.attractions.wanm.model_helper.pojo.Attraction



class BsvDescPresenter(private val view:Interface.View):Interface.Presenter {



    private val model:Interface.Model = BsvDescModel(this)

    override fun requestData(id:Int) {
        this.model.getData(id)
    }

    override fun callBackSuccess(attraction: Attraction) {
        this.view.setTitle(attraction.title)
        this.view.setDesc(attraction.desc)
        this.view.setImage(attraction.id)
    }

    override fun callBackError(t: Throwable) {
        this.view.setTitleError()
        this.view.setDescError()
        this.view.setImageError()
        Log.e("ERROR DESC",t.localizedMessage!!)
    }

    override fun callBackErrorBackend(responseStatus: Int) {
        //TODO:CODE ERROR
    }


}