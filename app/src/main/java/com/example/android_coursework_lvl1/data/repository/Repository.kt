package com.example.android_coursework_lvl1.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.android_coursework_lvl1.model.enums.FirstCountries
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.model.enums.SecondCountries
import com.example.android_coursework_lvl1.data.repository.network.models.ActorModel
import com.example.android_coursework_lvl1.data.repository.network.models.ImageModel
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel
import com.example.android_coursework_lvl1.data.repository.network.models.SearchMovieModel
import com.example.android_coursework_lvl1.data.repository.network.models.SeasonsModel
import com.example.android_coursework_lvl1.data.repository.network.models.StaffModel
import com.example.android_coursework_lvl1.data.repository.network.RetrofitKinopoisk


class Repository(context: Context) {

    companion object {
        private const val PREFERENCE_NAME = "PREFERENCE_NAME"
        private const val FIRST_COUNTRY_ID_KEY ="firstGenre"
        private const val SECOND_COUNTRY_ID_KEY ="secondCountryId"
        private const val FIRST_GENRE_KEY ="firstGenre"
        private const val SECOND_GENRE_KEY ="secondGenre"
    }
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val firstCountry: FirstCountries
        get() = FirstCountries.values().random()

    private val secondCountry: SecondCountries
        get() = SecondCountries.values().random()

    private val firstCountryId = sharedPreferences.getInt(FIRST_COUNTRY_ID_KEY, getFirstCountryId(firstCountry))

    private val secondCountryId = sharedPreferences.getInt(SECOND_COUNTRY_ID_KEY, getSecondCountryId(secondCountry))

    private val firstGenre = sharedPreferences.getInt(FIRST_GENRE_KEY, getFirstGenreId(firstCountryId))

    private val secondGenre = sharedPreferences.getInt(SECOND_GENRE_KEY, getSecondGenreId(secondCountryId))

    init {
        initializePreferences()
    }

    private fun initializePreferences() {
        val editor = sharedPreferences.edit()
        editor.putInt(FIRST_COUNTRY_ID_KEY, firstCountryId)
        editor.putInt(SECOND_COUNTRY_ID_KEY, secondCountryId)
        editor.putInt(FIRST_GENRE_KEY, firstGenre)
        editor.putInt(SECOND_GENRE_KEY, secondGenre)
        editor.apply()
    }

    private fun getFirstCountryId(country: FirstCountries): Int = when (country) {
        FirstCountries.USA -> 1
        FirstCountries.France -> 3
        else -> 5
    }

    private fun getSecondCountryId(country: SecondCountries): Int = when (country) {
        SecondCountries.Japan -> 16
        SecondCountries.USSR -> 33
        else -> 34
    }

    private fun getFirstGenreId(countryId: Int): Int = when (countryId) {
        1 -> 11 //USA Action
        3 -> 2 //FRENCH Drama
        else -> 13 //BRITISH Comedy
    }


    private fun getSecondGenreId(country: Int): Int = when (country) {
        16 -> 24 //JAPAN ANIME
        33 -> 14 //USSR MILITARY
        else -> 3 //RUSSIAN CRIMINAL
    }


    val categoryF = when (firstCountryId) {
        1 -> context.getString(R.string.usa_actions)
        3 -> context.getString(R.string.french_dramas)
        else -> context.getString(R.string.british_comedy)
    }


    val categoryS = when (secondCountryId) {
        16 -> context.getString(R.string.japan_anime)
        33 -> context.getString(R.string.soviet_military)
        else -> context.getString(R.string.russian_criminal)
    }


    suspend fun getFirstDynamicMovies(
        page: Int,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
    ): List<MovieModel> {
        val genres = sharedPreferences.getInt(FIRST_GENRE_KEY, getFirstGenreId(firstCountryId))
        val countries = sharedPreferences.getInt(FIRST_COUNTRY_ID_KEY, getFirstCountryId(firstCountry))
        return RetrofitKinopoisk.search.getFirstDynamicFilms(
            page,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo,
            genres,
            countries
        ).items
    }

    suspend fun getSecondDynamicMovies(
        page: Int,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
    ): List<MovieModel> {
        val genres = sharedPreferences.getInt(SECOND_GENRE_KEY, getSecondGenreId(secondCountryId))
        val countries = sharedPreferences.getInt(SECOND_COUNTRY_ID_KEY, getSecondCountryId(secondCountry))
        return RetrofitKinopoisk.search.getSecondDynamicFilms(
            page,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo,
            genres,
            countries
        ).items
    }

    suspend fun getPremieres(year: Int, month: String): List<MovieModel> {
        return RetrofitKinopoisk.search.getPremieres(year, month).items
    }

    suspend fun getPopular(page: Int): List<MovieModel> {
        return RetrofitKinopoisk.search.getPopular(
            page
        ).items
    }

    suspend fun getTop250(page: Int): List<MovieModel> {
        return RetrofitKinopoisk.search.getTop250(
            page
        ).items
    }

    suspend fun getSeries(
        page: Int,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
    ): List<MovieModel> {
        return RetrofitKinopoisk.search.getSeries(
            page,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo,
        ).items
    }


    suspend fun getInfo(id: Int?): MovieModel {
        return RetrofitKinopoisk.search.getInfo(id)
    }

    suspend fun getSeasons(id: Int?): List<SeasonsModel> {
        return RetrofitKinopoisk.search.getSeasons(id).items
    }

    suspend fun totalSeasons(id: Int?): Int {
        return RetrofitKinopoisk.search.getSeasons(id).total
    }

    suspend fun getStaff(filmId: Int?): List<StaffModel> {
        return RetrofitKinopoisk.search.getStaff(filmId)
    }

    suspend fun getActor(id: Int?): ActorModel {
        return RetrofitKinopoisk.search.getActor(id)
    }

    suspend fun getStillImage(id: Int?, page: Int): List<ImageModel> {
        return RetrofitKinopoisk.search.getStillImage(id, page).items
    }

    suspend fun getShootingImage(id: Int?, page: Int): List<ImageModel> {
        return RetrofitKinopoisk.search.getShootingImage(id, page).items
    }

    suspend fun getPosterImage(id: Int?, page: Int): List<ImageModel> {
        return RetrofitKinopoisk.search.getPosterImage(id, page).items
    }

    suspend fun getTotalStillImage(id: Int?): Int {
        return RetrofitKinopoisk.search.getTotalStillImage(id).total
    }

    suspend fun getTotalShootingImage(id: Int?): Int {
        return RetrofitKinopoisk.search.getTotalShootingImage(id).total
    }

    suspend fun getTotalPosterImage(id: Int?): Int {
        return RetrofitKinopoisk.search.getTotalPosterImage(id).total
    }

    suspend fun getSimilar(id: Int?): List<MovieModel> {
        return RetrofitKinopoisk.search.getSimilars(id).items
    }

    suspend fun customSearch(
        countries: Int?,
        genres: Int?,
        order: String?,
        type: String?,
        ratingFrom: Double?,
        ratingTo: Double?,
        yearFrom: Int?,
        yearTo: Int?,
        keyword: String?,
    ): List<SearchMovieModel> {
        return RetrofitKinopoisk.search.customSearchApi(
            countries,
            genres,
            order,
            type,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo,
            keyword,
        ).items
    }

    suspend fun searchKeywordApi(
        keywordFlow: String,
        page: Int
    ): List<SearchMovieModel> {
        return RetrofitKinopoisk.search.keywordSearchApi(keywordFlow,page).items
    }

}
