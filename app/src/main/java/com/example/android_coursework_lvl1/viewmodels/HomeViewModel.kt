package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android_coursework_lvl1.dataSource.FirstDynamicDataSource
import com.example.android_coursework_lvl1.dataSource.PopularDataSource
import com.example.android_coursework_lvl1.dataSource.SecondDynamicDataSource
import com.example.android_coursework_lvl1.dataSource.SeriesDataSource
import com.example.android_coursework_lvl1.dataSource.Top250DataSource
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application.applicationContext)

    private val top250DataSource: Top250DataSource = Top250DataSource(repository)
    private val popularDataSource: PopularDataSource = PopularDataSource(repository)
    private val firstDynamicDataSource: FirstDynamicDataSource = FirstDynamicDataSource(repository)
    private val secondDynamicDataSource: SecondDynamicDataSource =
        SecondDynamicDataSource(repository)
    private val seriesDataSource: SeriesDataSource = SeriesDataSource(repository)


    private val _premieresMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val premieresMovies = _premieresMovies.asStateFlow()
    private val _top250Movies = MutableStateFlow<List<MovieModel>>(emptyList())
    private val _firstDynamicMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    private val _secondDynamicMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    private val _popularMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    private val _seriesMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    private var firstRandomization = true
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    val pagedFirstDynamicMovies: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { firstDynamicDataSource }
    ).flow.cachedIn(viewModelScope)

    val pagedSecondDynamicMovies: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { secondDynamicDataSource }
    ).flow.cachedIn(viewModelScope)

    val pagedPopularMovies: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { popularDataSource }
    ).flow.cachedIn(viewModelScope)

    val pagedTop250Movies: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { top250DataSource }
    ).flow.cachedIn(viewModelScope)

    val pagedSeries: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { seriesDataSource }
    ).flow.cachedIn(viewModelScope)

    init {
        loadPremieres()
        loadPopular()
        loadTop250()
        loadFirstDynamicMovies()
        loadSecondDynamicMovies()
        loadSeries()
    }


    private fun loadPremieres() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = repository.getPremieres(2023, "JANUARY")
                _premieresMovies.value = movies
            } catch (error: Throwable) {
                Log.d("HomeViewModel", error.message ?: "")
            }
        }
    }

    private fun loadPopular() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = repository.getPopular(1)
                _popularMovies.value = movies
            } catch (error: Throwable) {
                Log.d("HomeViewModelPopular", error.message ?: "error")
            }
        }
    }

    private fun loadTop250() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = repository.getTop250(1)
                _top250Movies.value = movies
            } catch (error: Throwable) {
                Log.d("HomeViewModelTop250", error.message ?: "")
            }
        }
    }

    private fun loadSeries() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val series = repository.getSeries(1, 8, 10, 1000, 3000)
                _seriesMovies.value = series
            } catch (error: Throwable) {
                Log.d("HomeViewModelSeries", error.message ?: "")
            }
            _isLoading.value = false
        }
    }


    private fun loadFirstDynamicMovies() {
        if (firstRandomization) {
            viewModelScope.launch {
                try {
                    _isLoading.value = true
                    val movies =
                        repository.getFirstDynamicMovies(
                            1,
                            5,
                            10,
                            1000,
                            3000
                        )
                    _firstDynamicMovies.value = movies
                } catch (error: Throwable) {
                    Log.d("HomeViewModelFirstDynamic", error.message ?: "")
                }
            }
            firstRandomization = false
        }
    }

    private fun loadSecondDynamicMovies() {
        if (firstRandomization) {
            viewModelScope.launch {
                try {
                    val movies = repository.getSecondDynamicMovies(
                        1,
                        5,
                        10,
                        1000,
                        3000,
                    )
                    _secondDynamicMovies.value = movies
                } catch (error: Throwable) {
                    Log.d("HomeViewModelSecondDynamic", error.message ?: "")
                }
            }
            firstRandomization = false
        }
    }
}

