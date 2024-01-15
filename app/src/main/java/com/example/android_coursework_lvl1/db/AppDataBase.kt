package com.example.android_coursework_lvl1.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MyMovies::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun MyMoviesDao(): MyMoviesDao
}
