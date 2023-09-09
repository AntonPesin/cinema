package com.example.android_coursework_lvl1.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.repository.Repository


class Top250DataSource(private val repository: Repository) :
    PagingSource<Int, MovieModel>() {


    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return kotlin.runCatching {
            repository.getTop250(
                page,
            )
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure = { LoadResult.Error(it) }
        )
    }

    private companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}