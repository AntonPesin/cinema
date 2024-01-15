package com.example.android_coursework_lvl1.models

data class ActorModel(
    val personId: Int?,
    val nameRu: String?,
    val nameEn: String?,
    val sex: String?,
    val posterUrl: String?,
    val birthday : String?,
    val death: String?,
    val age : Int?,
    val birthplace:String?,
    val profession:String?,
    val films:List<Films>,
)
data class Films (
    val filmId:Int?,
    val nameRu: String?,
    val nameEn: String?,
    val rating:String?,
    val description:String?,
    val professionKey:String?,
)