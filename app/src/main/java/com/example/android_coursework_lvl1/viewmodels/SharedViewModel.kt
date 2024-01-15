package com.example.android_coursework_lvl1.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {

    private val _collections = MutableStateFlow<List<String>>(emptyList())
    val collections: StateFlow<List<String>> get() = _collections
    fun updateCollections(newCollections: List<String>) {
        _collections.value = newCollections
    }
}