package com.example.android_coursework_lvl1.models

data class SimilarModel(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val relationType: String?,
)
