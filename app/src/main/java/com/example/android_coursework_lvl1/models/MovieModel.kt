package com.example.android_coursework_lvl1.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.android_coursework_lvl1.additionalPages.Converters
@Entity(tableName = "movies")
@TypeConverters(Converters::class)
data class MovieModel(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    val kinopoiskId: Int?,
    val filmId: Int?,
    val imdbId:String?,
    @ColumnInfo(name = "nameRu")
    val nameRu: String?,
    @ColumnInfo(name = "nameEn")
    val nameEn: String?,
    @ColumnInfo(name = "nameOriginal")
    val nameOriginal: String?,
    @ColumnInfo(name = "genres")
    val genres: List<MovieGenres>?,
    @ColumnInfo(name = "countries")
    val countries: List<MovieCountry>?,
    @ColumnInfo(name = "poster")
    val posterUrl: String?,
    val posterUrlPreview: String?,
    @ColumnInfo(name = "ratingKinopoisk")
    val ratingKinopoisk: String?,
    @ColumnInfo(name = "ratingImdb")
    val ratingImdb: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "year")
    val year: Int?,
    @ColumnInfo(name = "filmLength")
    val filmLength: String?,
    @ColumnInfo(name = "ratingMpaa")
    val ratingMpaa: String?,
    @ColumnInfo(name = "relationType")
    val relationType: String?,
    @ColumnInfo(name = "ratingAgeLimits")
    val ratingAgeLimits: String?,
    val serial : Boolean?
)

data class MovieGenres(
    val genre: String,
)

data class MovieCountry(
    val country: String,
)

