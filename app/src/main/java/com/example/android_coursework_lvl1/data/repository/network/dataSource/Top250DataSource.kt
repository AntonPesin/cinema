package com.example.android_coursework_lvl1.data.repository.network.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android_coursework_lvl1.data.repository.Repository
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel

class Top250DataSource(private val repository: Repository) :
    PagingSource<Int, MovieModel>() {
    private val loadedItems = mutableSetOf<Int?>()

    override fun getRefreshKey(state: PagingState<Int, MovieModel>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return kotlin.runCatching {
            val items = repository.getTop250(page)
            val filteredItems = items.filter { it.kinopoiskId !in loadedItems }
            loadedItems.addAll(filteredItems.map { it.kinopoiskId })
            filteredItems
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