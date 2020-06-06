package com.attractions.wanm.model_helper.pojo

data class Attraction(
    val desc : String,
    val id : Int,
    val latitude : Double,
    val longitude : Double,
    val snippet : String,
    val title : String,
    val type : Int
)

data class ResponseAttraction(
    val attractions : List<Attraction>,
    val status : Int
)