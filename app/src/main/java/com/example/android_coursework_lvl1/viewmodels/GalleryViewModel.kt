package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.dataSource.GalleryDataSource
import com.example.android_coursework_lvl1.models.ImageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {


    var id: Int? = savedStateHandle["id"]
    private val repository: Repository = Repository(application.applicationContext)

    val pagedStillImages: Flow<PagingData<ImageModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { GalleryDataSource(repository, id, "still") }
    ).flow.cachedIn(viewModelScope)

    val pagedShootingImages: Flow<PagingData<ImageModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { GalleryDataSource(repository, id, "shooting") }
    ).flow.cachedIn(viewModelScope)

    val pagedPosterImages: Flow<PagingData<ImageModel>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { GalleryDataSource(repository, id, "poster") }
    ).flow.cachedIn(viewModelScope)
}