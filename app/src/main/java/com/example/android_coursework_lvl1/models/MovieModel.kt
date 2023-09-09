package com.example.android_coursework_lvl1.models

data class MovieModel(
    val kinopoiskId: Int?,
    val filmId: Int?,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val genres: List<MovieGenres>?,
    val countries: List<MovieCountry>?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val ratingKinopoisk: String?,
    val ratingImdb: String?,
    val description: String?,
    val year: Int?,
    val filmLength: Int?,
    val ratingMpaa: String?,
    val relationType: String?,
    val ratingAgeLimits: String?,
    val rating: Double?,
)

data class MovieGenres(
    val genre: String,
)

data class MovieCountry(
    val country: String,
)

