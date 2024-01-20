package com.example.android_coursework_lvl1.data.repository.network.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android_coursework_lvl1.data.repository.Repository
import com.example.android_coursework_lvl1.data.repository.network.models.ImageModel

class GalleryDataSource(
    private val repository: Repository,
    private val id: Int?,
    private val type: String,
) : PagingSource<Int, ImageModel>() {
    override fun getRefreshKey(state: PagingState<Int, ImageModel>): Int = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageModel> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return kotlin.runCatching {
            when (type) {
                STILL -> repository.getStillImage(id, page)
                SHOOTING -> repository.getShootingImage(id, page)
                POSTER -> repository.getPosterImage(id, page)
                else -> throw Exception("Invalid type: $type")
            }
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) }
        )
    }

    private companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val STILL = "still"
        private const val SHOOTING = "shooting"
        private const val POSTER = "poster"
    }
}

