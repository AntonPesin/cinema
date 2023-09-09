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
    val films:List<BestFilms>,
)

data class BestFilms (
    val filmId:Int?,
    val rating:String?
)