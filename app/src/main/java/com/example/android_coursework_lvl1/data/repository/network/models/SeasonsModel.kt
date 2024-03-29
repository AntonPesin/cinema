package com.example.android_coursework_lvl1.data.repository.network.models

data class SeasonsModel(
    val number: Int,
    val episodes: List<SeasonsEpisodes>?,
)

data class SeasonsEpisodes(
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val nameRu: String?,
    val nameEn: String?,
    val synopsis: String?,
    val releaseDate: String?,
)
