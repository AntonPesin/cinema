package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.models.ActorModel
import com.example.android_coursework_lvl1.models.Films
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ActorViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {


    private val repository: Repository = Repository(application.applicationContext)
    val id: Int? = savedStateHandle["id"]

    private val _bestFilms = MutableStateFlow<List<Films>>(emptyList())
    val bestFilms = _bestFilms.asStateFlow()
    suspend fun getData(): ActorModel {
        Log.d("ActorViewModel", "$id")
        return repository.getActor(id)
    }

}