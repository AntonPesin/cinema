package com.example.android_coursework_lvl1.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.repository.Repository

class PopularDataSource(private val repository: Repository) :
    PagingSource<Int, MovieModel>() {

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return kotlin.runCatching {
            repository.getPopular(
                page = pageIndex,
            )
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                    nextKey = if (it.isEmpty()) null else pageIndex + 1
                )
            }, onFailure = { LoadResult.Error(it) }
        )
    }

    private companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}