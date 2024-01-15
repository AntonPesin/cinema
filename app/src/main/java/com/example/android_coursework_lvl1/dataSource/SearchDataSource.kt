package com.example.android_coursework_lvl1.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.models.SearchMovieModel
import kotlinx.coroutines.flow.StateFlow

class SearchDataSource(
    private val repository: Repository,
    private var keyword: String,
    private var hideWatched: StateFlow<Boolean>,
    private var seenMovies: StateFlow<List<MovieModel>>,
) :
    PagingSource<Int, SearchMovieModel>() {
    private val loadedItems = mutableSetOf<Int>()

    override fun getRefreshKey(state: PagingState<Int, SearchMovieModel>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchMovieModel> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return kotlin.runCatching {
            val currentKeyword = keyword
            val items = repository.searchKeywordApi(currentKeyword, page)
            val filteredItems = items.filter { it.kinopoiskId !in loadedItems }
            loadedItems.addAll(filteredItems.map { it.kinopoiskId })
            if (hideWatched.value) {
                val unseenItems = filteredItems.filterNot { movie ->
                    seenMovies.value.any { it.kinopoiskId == movie.kinopoiskId }
                }
                loadedItems.addAll(unseenItems.map { it.kinopoiskId })
                unseenItems
            } else {
                loadedItems.addAll(filteredItems.map { it.kinopoiskId })
                filteredItems
            }
        }.fold(
            onSuccess =
            {
                LoadResult.Page(
                    data = it,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            }, onFailure =
            { LoadResult.Error(it) }
        )
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}


