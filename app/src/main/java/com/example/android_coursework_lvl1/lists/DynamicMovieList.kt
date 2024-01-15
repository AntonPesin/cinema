package com.example.android_coursework_lvl1.lists

import com.example.android_coursework_lvl1.models.MovieModel

class DynamicMovieList(
    val total: Int,
    val totalPages: Int,
    val items: List<MovieModel>,
)