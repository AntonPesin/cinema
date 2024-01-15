package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class SearchSettingsViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("search_settings")
    }

    private object PreferencesKeys {
        val TYPE = stringPreferencesKey("type")
        val COUNTRY = stringPreferencesKey("country")
        val GENRE = stringPreferencesKey("genre")
        val YEAR_FROM = intPreferencesKey("year_from")
        val YEAR_TO = intPreferencesKey("year_to")
        val RATING_FROM = doublePreferencesKey("rating_from")
        val RATING_TO = doublePreferencesKey("rating_to")
        val ORDER = stringPreferencesKey("order")
        val WATCHED = booleanPreferencesKey("watched")
    }

    suspend fun saveDataToDataStore(
        type: String,
        country: String,
        genre: String,
        yearFrom: Int,
        yearTo: Int,
        ratingFrom: Double,
        ratingTo: Double,
        order: String,
        hideWatched: Boolean,
    ) {
        getApplication<Application>().dataStore.edit { preferences ->
            preferences[PreferencesKeys.TYPE] = type
            preferences[PreferencesKeys.COUNTRY] = country
            preferences[PreferencesKeys.GENRE] = genre
            preferences[PreferencesKeys.YEAR_FROM] = yearFrom
            preferences[PreferencesKeys.YEAR_TO] = yearTo
            preferences[PreferencesKeys.RATING_FROM] = ratingFrom
            preferences[PreferencesKeys.RATING_TO] = ratingTo
            preferences[PreferencesKeys.ORDER] = order
            preferences[PreferencesKeys.WATCHED] = hideWatched
        }
    }

    val searchData: Flow<SearchData> =
        getApplication<Application>().dataStore.data.map { preferences ->
            SearchData(
                type = preferences[PreferencesKeys.TYPE] ?: "ALL",
                country = preferences[PreferencesKeys.COUNTRY] ?: "Абхазия",
                genre = preferences[PreferencesKeys.GENRE] ?: "триллер",
                yearFrom = preferences[PreferencesKeys.YEAR_FROM] ?: 1900,
                yearTo = preferences[PreferencesKeys.YEAR_TO] ?: Calendar.getInstance()
                    .get(Calendar.YEAR),
                ratingFrom = preferences[PreferencesKeys.RATING_FROM] ?: 1.0,
                ratingTo = preferences[PreferencesKeys.RATING_TO] ?: 10.0,
                order = preferences[PreferencesKeys.ORDER] ?: "YEAR",
                watched = preferences[PreferencesKeys.WATCHED] ?: false
            )
        }

    data class SearchData(
        var type: String,
        var country: String,
        var genre: String,
        var yearFrom: Int,
        var yearTo: Int,
        var ratingFrom: Double,
        var ratingTo: Double,
        var order: String,
        var watched: Boolean,
    )

    suspend fun clearDataStore() {
        getApplication<Application>().dataStore.edit { it.clear() }
    }
}