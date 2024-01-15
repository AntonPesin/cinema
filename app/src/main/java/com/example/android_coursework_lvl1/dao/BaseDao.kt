package com.example.android_coursework_lvl1.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_coursework_lvl1.models.MovieModel

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T)

    @Query("DELETE FROM movies WHERE movieId =:movieId")
    fun delete(movieId: Int?)

    @Query("DELETE  FROM movies ")
    fun deleteAll()

    @Query("SELECT * FROM movies WHERE movieId =:movieId")
    fun get(movieId: Int?): T


    @Query("SELECT * FROM movies")
    fun getAll(): List<T>

    @Query("SELECT * FROM movies")
    fun getAllSeenIds(): List<MovieModel>

    @Query("SELECT COUNT(*) FROM movies WHERE filmId = :movieId")
    fun exists(movieId: Int?): Boolean
}