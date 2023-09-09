package com.example.android_coursework_lvl1.retrofit

import com.example.android_coursework_lvl1.lists.DynamicMovieList
import com.example.android_coursework_lvl1.lists.ImageList
import com.example.android_coursework_lvl1.lists.PopularMovieList
import com.example.android_coursework_lvl1.lists.PremiereMovieList
import com.example.android_coursework_lvl1.lists.SeasonsList
import com.example.android_coursework_lvl1.lists.SeriesList
import com.example.android_coursework_lvl1.lists.SimilarList
import com.example.android_coursework_lvl1.lists.Top250MovieList
import com.example.android_coursework_lvl1.models.ActorModel
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.models.StaffModel
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


//67ed8b98-11a6-41b4-ae18-a6d55865c27f darfow74@gmail.com
//74d1542b-a48d-4738-82a3-83feacf5f065 antopesin

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
    @GET("/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun getPopular(
        @Query("page") page: Int,
    ): PopularMovieList

    //Get top250 films
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/top?type=TOP_250_BEST_FILMS")
    suspend fun getTop250(
        @Query("page") page: Int,
    ): Top250MovieList

    //Get series
    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films?order=RATING&type=TV_SERIES")
    suspend fun getSeries(
        @Query("page") page: Int,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
    ): SeriesList


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
    ): DynamicMovieList

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
    ): DynamicMovieList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}")
    suspend fun getInfo(
        @Path("id") id: Int?,
    ): MovieModel

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun getSeasons(
        @Path("id") id: Int?,
    ): SeasonsList


    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v1/staff")
    suspend fun getStaff(
        @Query("filmId") filmId: Int?,
    ): List<StaffModel>

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v1/staff/{id}")
    suspend fun getActor(
        @Path("id") id: Int?,
    ): ActorModel

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v1/staff/{id}")
    suspend fun getBestMovies(
        @Path("id") id: Int?,
    ): List<MovieModel>

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=STILL&page=1")
    suspend fun getStillImage(@Path("id") id: Int?): ImageList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=SHOOTING&page=1")
    suspend fun getShootingImage(@Path("id") id: Int?): ImageList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/images?type=POSTER&page=1")
    suspend fun getPosterImage(@Path("id") id: Int?): ImageList

    @Headers("X-API-KEY: $API_KEY")
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilars(@Path("id") id: Int?): SimilarList



    private companion object {
                const val API_KEY = "74d1542b-a48d-4738-82a3-83feacf5f065"

    }
}