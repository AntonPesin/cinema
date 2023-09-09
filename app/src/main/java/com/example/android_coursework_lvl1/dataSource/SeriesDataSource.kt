package com.example.android_coursework_lvl1.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.repository.Repository

class SeriesDataSource(private val repository: Repository) :
    PagingSource<Int, MovieModel>() {

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int? = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return kotlin.runCatching {
            repository.getSeries(
                page,
                ratingFrom = 8,
                ratingTo = 10,
                yearFrom = 1000,
                yearTo = 3000,

            )
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