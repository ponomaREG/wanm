package com.attractions.wanm.model_helper

import com.attractions.wanm.model_helper.pojo.ResponseAttraction
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class Network {

    private var retrofit:Retrofit? = null
    private val baseURL = "http://161.35.108.15:1111"

    companion object{
        private var instance:Network? = null

        fun getInstance():Network{
            if(instance == null){
                instance = Network()
            }
            return instance as Network
        }
    }


    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    fun getReposAttractions():ReposAttractions{
        return retrofit!!.create(ReposAttractions::class.java)
    }


    interface ReposAttractions{
        @GET("/attractions")
        fun getResponseAttraction():Observable<ResponseAttraction>

        @GET("/attractions")
        fun getResponseAttraction(@Query("id") id:Int):Observable<ResponseAttraction>
    }





}