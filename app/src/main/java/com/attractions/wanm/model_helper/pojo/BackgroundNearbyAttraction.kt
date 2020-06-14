package com.attractions.wanm.model_helper.pojo

class BackgroundNearbyAttraction {



    data class NearbyAttraction(
        val id:Int,
        val title:String,
        val distance:Int
        )

    data class ResponseNearbyAttraction(
        val status:Int,
        val nearAttraction: NearbyAttraction
    )
}