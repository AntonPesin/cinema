package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.android_coursework_lvl1.App.Companion.INSTANCE
import com.example.android_coursework_lvl1.dao.BaseDao
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.models.ImageModel
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.models.StaffModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    private val repository = Repository(application.applicationContext)

    private val _idState = MutableStateFlow(savedStateHandle["id"] ?: 0)
    val idState: StateFlow<Int> = _idState

    private var _isLoading = MutableStateFlow(false)
    var isLoading: StateFlow<Boolean> = _isLoading

    var id: Int? = savedStateHandle["id"]

    val favoriteDao = INSTANCE.favoriteDataBase.favoriteDao()
    val wantSeeDao = INSTANCE.wantSeeDataBase.wantSeeDao()
    val seenDao = INSTANCE.seenDataBase.seenDao()
    private val interestingDao = INSTANCE.interestingDataBase.interestingDao()

    fun setNewId(id: Int?) {
        _isLoading.value = true
        viewModelScope.launch {
            id?.let {
                _idState.value = it
            }
        }
        _isLoading.value = false
    }

    private fun roomCheck(id: Int?): String {
        return daoMap.entries.find { it.value.exists(id) }?.key ?: "repo"
    }

    suspend fun getInfo(id: Int?): MovieModel {

        val daoKey = roomCheck(id)
        return if (daoKey != "repo") {
            daoMap[daoKey]!!.get(id)
        } else {
            repository.getInfo(id)

        }
    }

    suspend fun getSeasons(id: Int?): Int {
        return repository.totalSeasons(id)
    }

    suspend fun getStaff(id: Int?): List<StaffModel> {
        return repository.getStaff(id)
    }

    suspend fun getImages(id: Int?, page: Int): List<ImageModel> {
        return repository.getStillImage(id, page)
    }

    suspend fun getTotalImages(id: Int?): Int {
        return repository.getTotalStillImage(id) + repository.getTotalShootingImage(id) + repository.getTotalPosterImage(
            id
        )
    }

    suspend fun getSimilars(id: Int?): List<MovieModel> {
        return repository.getSimilar(id)
    }

    private val daoMap = mapOf(
        "FAVORITE" to favoriteDao,
        "WANT_SEE" to wantSeeDao,
        "SEEN" to seenDao,
        "INTERESTING" to interestingDao
    )

    private suspend fun createMovieModel(): MovieModel {
        val id = idState.value
        val info = getInfo(id)
        return MovieModel(
            kinopoiskId = id,
            filmId = id,
            imdbId = info.imdbId,
            nameRu = info.nameRu.toString(),
            nameEn = info.nameEn.toString(),
            nameOriginal = info.nameOriginal.toString(),
            genres = info.genres,
            countries = info.countries,
            posterUrl = info.posterUrl,
            posterUrlPreview = info.posterUrlPreview,
            ratingKinopoisk = info.ratingKinopoisk.toString(),
            ratingImdb = info.ratingImdb.toString(),
            description = info.description.toString(),
            year = info.year,
            filmLength = info.filmLength,
            ratingMpaa = info.ratingMpaa.toString(),
            ratingAgeLimits = info.ratingAgeLimits.toString(),
            relationType = info.relationType.toString(),
            serial = info.serial
        )
    }

    private suspend fun insertMovie(model: MovieModel, dao: BaseDao<MovieModel>) {
        withContext(Dispatchers.IO) {
            dao.insert(model)
        }
    }

    private suspend fun deleteMovie(id: Int?, dao: BaseDao<MovieModel>) {
        withContext(Dispatchers.IO) {
            dao.delete(id)
        }
    }


    private fun performDaoOperation(operation: suspend () -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                operation()
            }
        }
    }

    fun addFavorite() {
        performDaoOperation {
            val movieModel = createMovieModel()
            insertMovie(movieModel, favoriteDao)
        }
    }

    fun deleteFavorite() {
        performDaoOperation {
            deleteMovie(id, favoriteDao)
        }
    }

    fun addWantSee() {
        performDaoOperation {
            val movieModel = createMovieModel()
            insertMovie(movieModel, wantSeeDao)
        }
    }

    fun deleteWantSee() {
        performDaoOperation {
            deleteMovie(id, wantSeeDao)
        }
    }

    fun addSeen() {
        performDaoOperation {
            val movieModel = createMovieModel()
            insertMovie(movieModel, seenDao)
        }
    }

    fun deleteSeen() {
        performDaoOperation {
            deleteMovie(id, seenDao)
        }
    }

    fun addInteresting() {
        performDaoOperation {
            if (interestingDao.getAll().size >= 10) {
                interestingDao.delete(interestingDao.getAll().last().kinopoiskId)
            } else {
                val movieModel = createMovieModel()
                insertMovie(movieModel, interestingDao)
            }
        }
    }
}




