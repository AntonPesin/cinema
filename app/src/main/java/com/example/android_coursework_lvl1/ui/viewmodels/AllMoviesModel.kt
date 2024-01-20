package com.example.android_coursework_lvl1.ui.viewmodels

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android_coursework_lvl1.data.repository.Repository
import com.example.android_coursework_lvl1.data.repository.network.dataSource.FirstDynamicDataSource
import com.example.android_coursework_lvl1.data.repository.network.dataSource.PopularDataSource
import com.example.android_coursework_lvl1.data.repository.network.dataSource.SecondDynamicDataSource
import com.example.android_coursework_lvl1.data.repository.network.dataSource.SeriesDataSource
import com.example.android_coursework_lvl1.data.repository.network.dataSource.Top250DataSource
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AllMoviesModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val repository: Repository = Repository(application.applicationContext)

    private val popularDataSource: PopularDataSource = PopularDataSource(repository)
    private val top250DataSource: Top250DataSource = Top250DataSource(repository)
    private val firstDynamicDataSource: FirstDynamicDataSource = FirstDynamicDataSource(repository)
    private val secondDynamicDataSource: SecondDynamicDataSource =
        SecondDynamicDataSource(repository)
    private val seriesDataSource: SeriesDataSource = SeriesDataSource(repository)
    private val _premierMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    val premierMovies = _premierMovies.asStateFlow()

    init {
        loadPremieres()
    }

    val pagedPopularMovies: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { popularDataSource }
    ).flow.cachedIn(viewModelScope)

    val pagedTop250Movies: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { top250DataSource }
    ).flow.cachedIn(viewModelScope)

    val pagedFirstDynamicMovies: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { firstDynamicDataSource }
    ).flow.cachedIn(viewModelScope)

    val pagedSecondDynamicMovies: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { secondDynamicDataSource }
    ).flow.cachedIn(viewModelScope)

    val pagedSeries: Flow<PagingData<MovieModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { seriesDataSource }
    ).flow.cachedIn(viewModelScope)

    private fun loadPremieres() {
        val calendar = Calendar.getInstance()
        viewModelScope.launch {
            try {
                val movies = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    repository.getPremieres(
                        LocalDate.now().year,
                        LocalDate.now().plusDays(14).month.name
                    )
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
}