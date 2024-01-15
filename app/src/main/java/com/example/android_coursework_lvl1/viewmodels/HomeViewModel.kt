package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.models.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {



    private val repository: Repository = Repository(application.applicationContext)

    private val _premierMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val premierMovies = _premierMovies.asStateFlow()

    private val _top250LimitedMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val top250LimitedMovies = _top250LimitedMovies.asStateFlow()

    private val _popularLimitedMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val popularLimitedMovies = _popularLimitedMovies.asStateFlow()

    private val _firstDynamicLimitedMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val firstDynamicLimitedMovies = _firstDynamicLimitedMovies.asStateFlow()

    private val _secondDynamicLimitedMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val secondDynamicLimitedMovies = _secondDynamicLimitedMovies.asStateFlow()

    private val _seriesLimitedMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val seriesLimitedMovies = _seriesLimitedMovies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadPremieres()
        loadPopular()
        load250()
        loadFirstDynamicMovies()
        loadSecondDynamicMovies()
        loadSeries()
    }

    private fun loadPremieres() {
        val calendar = Calendar.getInstance()
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val twoWeeksAhead = LocalDate.now().plusDays(14)
                    repository.getPremieres(twoWeeksAhead.year, twoWeeksAhead.month.name)
                } else {
                    repository.getPremieres(
                        calendar.get(Calendar.YEAR),
                        calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                    )
                }
                _premierMovies.value = movies
            } catch (error: Throwable) {
                Log.d("HomeViewModelPremier", error.message ?: "")
            }
        }
    }

    private fun load250() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = repository.getTop250(1)
                _top250LimitedMovies.value = movies
            } catch (error: Throwable) {
                Log.d("HomeViewModel250", error.message ?: "")
            }
        }
    }

    private fun loadPopular() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = repository.getPopular(1)
                _popularLimitedMovies.value = movies
            } catch (error: Throwable) {
                Log.d("HomeViewModelPopular", error.message ?: "error")
            }
        }
    }

    private fun loadFirstDynamicMovies() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = repository.getFirstDynamicMovies(
                    1,
                    5,
                    10,
                    1000,
                    3000
                )
                _firstDynamicLimitedMovies.value = movies
            } catch (error: Throwable) {
                Log.d("HomeViewModelFirstDynamic", error.message ?: "")
            }
        }
    }

    private fun loadSecondDynamicMovies() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = repository.getSecondDynamicMovies(
                    1,
                    5,
                    10,
                    1000,
                    3000,
                )
                _secondDynamicLimitedMovies.value = movies
            } catch (error: Throwable) {
                Log.d("HomeViewModelSecondDynamic", error.message ?: "")
            }
        }
    }

    private fun loadSeries() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val series = repository.getSeries(1, 8, 10, 1000, 3000)
                _seriesLimitedMovies.value = series
            } catch (error: Throwable) {
                Log.d("HomeViewModelSeries", error.message ?: "")
            }
            _isLoading.value = false
        }
    }
}



