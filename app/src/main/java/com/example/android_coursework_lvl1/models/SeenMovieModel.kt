package com.example.android_coursework_lvl1.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "seen_movies")
data class SeenMovieModel (
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    val kinopoiskId: Int?,
    val filmId: Int?,
)