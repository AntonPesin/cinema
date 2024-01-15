package com.example.android_coursework_lvl1.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.models.ImageModel

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
                "still" -> repository.getStillImage(id, page)
                "shooting" -> repository.getShootingImage(id, page)
                "poster" -> repository.getPosterImage(id, page)
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
    }
}

