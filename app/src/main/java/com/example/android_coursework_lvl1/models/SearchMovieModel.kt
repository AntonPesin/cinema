package com.example.android_coursework_lvl1.models
data class SearchMovieModel (
    val kinopoiskId:Int,
    val imdbId:String?,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val countries: List<SearchMovieCountry>?,
    val genres: List<SearchMovieGenres>?,
    val ratingKinopoisk: Double?,
    val ratingImdb: Double?,
    val year: Int?,
    val type:String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
)

data class SearchMovieGenres(
    val genre: String,
)

data class SearchMovieCountry(
    val country: String,
)