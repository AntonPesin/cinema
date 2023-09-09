package com.example.android_coursework_lvl1.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MyMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(myMovies: MyMovies)

    @Query("SELECT * FROM MyMovies")
    fun getMovies(): Flow<List<MyMovies>>

}
