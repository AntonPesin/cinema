package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.android_coursework_lvl1.models.ImageModel
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.models.SimilarModel
import com.example.android_coursework_lvl1.models.StaffModel
import com.example.android_coursework_lvl1.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SerialViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val repository = Repository(application.applicationContext)
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _idState = MutableStateFlow(savedStateHandle["id"] ?: 0)
    val idState: StateFlow<Int> = _idState

    fun loadSerialPageData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
            } catch (error: Throwable) {
                Log.d("SerialPageViewModel", error.message ?: "")
            } finally {
                _isLoading.value = false
            }
        }
    }


    var id: Int? = savedStateHandle["id"]

    suspend fun getInfo(newId:Int): MovieModel {
        _isLoading.value = true
        try {
            Log.d("getInfo", "$newId")
        } catch (e: Exception) {
            Log.d("getInfo", "error")
        }
        _isLoading.value = false
        return repository.getInfo(newId)
    }

    suspend fun getSeasons(newId:Int): Int {
        Log.d("getSeasons", "$newId")
        return repository.totalSeasons(newId)
    }

    suspend fun getStaff(newId:Int): List<StaffModel> {
        Log.d("getStaff", "$newId")
        return repository.getStaff(newId)
    }

    suspend fun getImages(newId:Int): List<ImageModel> {
        Log.d("getImages", "$newId")
        return repository.getStillImage(newId)
    }

    suspend fun getTotalImages(newId:Int): Int {
        val total =
            repository.getShootingImage(id).size + repository.getTotalStillImage(newId) + repository.getPosterImage(
                newId
            ).size
        Log.d("getAllImages", "${repository.getTotalStillImage(newId)}")
        return (total)
    }

    suspend fun getSimilars(id:Int): List<SimilarModel> {
        Log.d("getSimilars", "$id")
        return repository.getSimilar(id)
    }

    fun setNewId(newId: Int?) {
        newId?.let {
            _idState.value = it
        }
    }

}

