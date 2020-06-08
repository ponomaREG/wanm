package com.attractions.wanm.fragments.descAttraction

import android.graphics.Bitmap
import com.attractions.wanm.model_helper.pojo.Attraction

interface Interface {



    interface View{
        fun setTitle(title:String)
        fun setDesc(desc:String)
        fun setImage(id:Int)
        fun setImageError()
        fun setTitleError()
        fun setDescError()
        fun showToastError()
    }

    interface Presenter{
        fun requestData(id:Int)
        fun callBackSuccess(attraction: Attraction)
        fun callBackError(t:Throwable)
        fun callBackErrorBackend(responseStatus:Int)
    }

    interface Model{
        fun getData(id:Int)
    }
}