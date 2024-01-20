package com.example.android_coursework_lvl1.data.repository.network

import com.example.android_coursework_lvl1.data.repository.network.lists.ImageList
import com.example.android_coursework_lvl1.data.repository.network.lists.MovieList
import com.example.android_coursework_lvl1.data.repository.network.lists.PremiereMovieList
import com.example.android_coursework_lvl1.data.repository.network.lists.SearchMovieList
import com.example.android_coursework_lvl1.data.repository.network.lists.SeasonsList
import com.example.android_coursework_lvl1.data.repository.network.lists.SimilarList
import com.example.android_coursework_lvl1.data.repository.network.models.ActorModel
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel
import com.example.android_coursework_lvl1.data.repository.network.models.StaffModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitKinopoisk {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofitKinopoisk = Retrofit
        .Builder()
        .client(
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build()
        )
        .baseUrl("https://kinopoiskapiunofficial.tech")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val search: KinopoiskApiService = retrofitKinopoisk.create(KinopoiskApiService::class.java)

}

interface KinopoiskApiService {

    //Get premieres
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/premieres")
    suspend fun getPremieres(
        @Query("year") year: Int,
        @Query("month") month: String,
    ): PremiereMovieList

    //Get popular films
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/collections?type=TOP_POPULAR_MOVIES")
    suspend fun getPopular(
        @Query("page") page: Int,
    ): MovieList

    //Get top250 films
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/collections?type=TOP_250_MOVIES")
    suspend fun getTop250(
        @Query("page") page: Int,
    ): MovieList

    //Get series
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films?order=RATING&type=TV_SERIES")
    suspend fun getSeries(
        @Query("page") page: Int,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
    ): MovieList


    //Get FirstDynamic films
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films?type=FILM")
    suspend fun getFirstDynamicFilms(
        @Query("page") page: Int,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("genres") genres: Int,
        @Query("countries") countries: Int,
    ): MovieList

    //Get SecondDynamic films
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films?type=FILM")
    suspend fun getSecondDynamicFilms(
        @Query("page") page: Int,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("genres") genres: Int,
        @Query("countries") countries: Int,
    ): MovieList

    //Get MovieInfo
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}")
    suspend fun getInfo(
        @Path("id") id: Int?,
    ): MovieModel

    //Get seasons
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun getSeasons(
        @Path("id") id: Int?,
    ): SeasonsList

    //Get staff
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v1/staff")
    suspend fun getStaff(
        @Query("filmId") filmId: Int?,
    ): List<StaffModel>

    //Get actor
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v1/staff/{id}")
    suspend fun getActor(
        @Path("id") id: Int?,
    ): ActorModel


    //Get still image
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=STILL")
    suspend fun getStillImage(
        @Path("id") id: Int?,
        @Query("page") page: Int,
    ): ImageList

    //Get shooting image
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=SHOOTING")
    suspend fun getShootingImage(
        @Path("id") id: Int?,
        @Query("page") page: Int,
    ): ImageList

    //Get poster image
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=POSTER")
    suspend fun getPosterImage(
        @Path("id") id: Int?,
        @Query("page") page: Int,
    ): ImageList

    //Get totalstill image
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=STILL&page=1")
    suspend fun getTotalStillImage(
        @Path("id") id: Int?,
    ): ImageList

    //Get totalshooting image
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=SHOOTING&page=1")
    suspend fun getTotalShootingImage(
        @Path("id") id: Int?,
    ): ImageList

    //Get totalposter image
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=POSTER&page=1")
    suspend fun getTotalPosterImage(
        @Path("id") id: Int?,
    ): ImageList

    //Get similars
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilars(@Path("id") id: Int?): SimilarList

    //com.example.android_coursework_lvl1.NavigationPages.Search
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films")
    suspend fun customSearchApi(
        @Query("countries") countries: Int?,
        @Query("genres") genres: Int?,
        @Query("order") order: String?,
        @Query("type") type: String?,
        @Query("ratingFrom") ratingFrom: Double?,
        @Query("ratingTo") ratingTo: Double?,
        @Query("yearFrom") yearFrom: Int?,
        @Query("yearTo") yearTo: Int?,
        @Query("keyword") keyword: String?,
    ): SearchMovieList

    //DefaultSearch
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films")
    suspend fun keywordSearchApi(
        @Query("keyword") keyword: String?,
        @Query("page") page: Int,
    ): SearchMovieList


    //67ed8b98-11a6-41b4-ae18-a6d55865c27f darfow74@gmail.com
    //74d1542b-a48d-4738-82a3-83feacf5f065 antopesin
    private companion object {
        const val API_KEY = "74d1542b-a48d-4738-82a3-83feacf5f065"
    }
}