package com.example.android_coursework_lvl1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android_coursework_lvl1.additionalPages.Converters
import com.example.android_coursework_lvl1.dao.FirstCollectionDao
import com.example.android_coursework_lvl1.models.MovieModel

@Database(entities = [MovieModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)

abstract class FirstCustomDataBase : RoomDatabase() {
    abstract fun firstCustomDao(): FirstCollectionDao
}