package com.example.android_coursework_lvl1.additionalPages

import androidx.room.TypeConverter
import com.example.android_coursework_lvl1.models.MovieCountry
import com.example.android_coursework_lvl1.models.MovieGenres
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromGenreString(value: String?): List<MovieGenres>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<MovieGenres>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toGenreString(value: List<MovieGenres>?): String? {
        if (value == null) {
            return null
        }
        return Gson().toJson(value)
    }

    @TypeConverter
    fun fromCountryString(value: String?): List<MovieCountry>? {
        if (value == null) {
            return null
        }
        val listType = object : TypeToken<List<MovieCountry>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toCountryString(value: List<MovieCountry>?): String? {
        if (value == null) {
            return null
        }
        return Gson().toJson(value)
    }
}
