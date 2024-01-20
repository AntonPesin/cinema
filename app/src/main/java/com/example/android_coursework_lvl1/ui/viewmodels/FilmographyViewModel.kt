package com.example.android_coursework_lvl1.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.android_coursework_lvl1.data.repository.Repository
import com.example.android_coursework_lvl1.data.repository.network.models.ActorModel
import com.example.android_coursework_lvl1.data.repository.network.models.Films
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class FilmographyViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application.applicationContext)
    val id: Int? = savedStateHandle["id"]

    private val _bestFilms = MutableStateFlow<List<Films>>(emptyList())
    val bestFilms = _bestFilms.asStateFlow()

    suspend fun getData(id: Int?): ActorModel {
        Log.d("FilmographyViewModel", "$id")
        return repository.getActor(id)
    }

}