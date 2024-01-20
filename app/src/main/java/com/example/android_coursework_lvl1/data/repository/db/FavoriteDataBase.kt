package com.example.android_coursework_lvl1.data.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android_coursework_lvl1.utils.converter.Converters
import com.example.android_coursework_lvl1.data.repository.db.dao.FavoriteDao

import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel

@Database(entities = [MovieModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FavoriteDataBase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}