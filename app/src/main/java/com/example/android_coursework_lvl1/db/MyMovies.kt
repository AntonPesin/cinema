package com.example.android_coursework_lvl1.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MyMovies")
data class MyMovies(
    @PrimaryKey
    val name: String
)
