package com.example.android_coursework_lvl1.data.repository.db.dao

import androidx.room.Dao
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel

@Dao
interface FirstCollectionDao : BaseDao<MovieModel>