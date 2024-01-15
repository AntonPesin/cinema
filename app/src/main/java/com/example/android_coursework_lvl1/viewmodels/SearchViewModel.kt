package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android_coursework_lvl1.App
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.dataSource.SearchDataSource
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.models.SearchMovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {
    private val repository: Repository = Repository(application.applicationContext)

    private val _keyword = MutableStateFlow("")
    private val keyword: MutableStateFlow<String> = _keyword

    private val seenDao = App.INSTANCE.seenDataBase.seenDao()

    private val _hideWatched = MutableStateFlow(true)
    private val hideWatched: StateFlow<Boolean> = _hideWatched

    private val _seenMovies = MutableStateFlow<List<MovieModel>>(emptyList())
    private val seenMovies = _seenMovies.asStateFlow()

    val pagedSearchMovies: Flow<PagingData<SearchMovieModel>> by lazy {
        keyword.flatMapLatest { keyword ->
            Pager(config = PagingConfig(pageSize = 20),
                pagingSourceFactory = {
                    SearchDataSource(
                        repository,
                        keyword,
                        hideWatched,
                        seenMovies
                    )
                }
            ).flow.cachedIn(viewModelScope)
        }
    }

    fun getSeenMovies() {
        viewModelScope.launch {
            _seenMovies.value = withContext(Dispatchers.IO) {
                seenDao.getAllSeenIds()
            }
        }
    }

    fun setKeyword(keyword: String) {
        _keyword.value = keyword
    }

    fun setHideWatched(state: Boolean) {
        _hideWatched.value = state

    }

    val genresList = listOf(
        mapOf("id" to 1, "genre" to "триллер"),
        mapOf("id" to 2, "genre" to "драма"),
        mapOf("id" to 3, "genre" to "криминал"),
        mapOf("id" to 4, "genre" to "мелодрама"),
        mapOf("id" to 5, "genre" to "детектив"),
        mapOf("id" to 6, "genre" to "фантастика"),
        mapOf("id" to 7, "genre" to "приключения"),
        mapOf("id" to 8, "genre" to "биография"),
        mapOf("id" to 9, "genre" to "фильм-нуар"),
        mapOf("id" to 10, "genre" to "вестерн"),
        mapOf("id" to 11, "genre" to "боевик"),
        mapOf("id" to 12, "genre" to "фэнтези"),
        mapOf("id" to 13, "genre" to "комедия"),
        mapOf("id" to 14, "genre" to "военный"),
        mapOf("id" to 15, "genre" to "история"),
        mapOf("id" to 16, "genre" to "музыка"),
        mapOf("id" to 17, "genre" to "ужасы"),
        mapOf("id" to 18, "genre" to "мультфильм"),
        mapOf("id" to 19, "genre" to "семейный"),
        mapOf("id" to 20, "genre" to "мюзикл"),
        mapOf("id" to 21, "genre" to "спорт"),
        mapOf("id" to 22, "genre" to "документальный"),
        mapOf("id" to 23, "genre" to "короткометражка"),
        mapOf("id" to 24, "genre" to "аниме"),
        mapOf("id" to 26, "genre" to "новости"),
        mapOf("id" to 27, "genre" to "концерт"),
        mapOf("id" to 28, "genre" to "для взрослых"),
        mapOf("id" to 29, "genre" to "церемония"),
        mapOf("id" to 30, "genre" to "реальное ТВ"),
        mapOf("id" to 31, "genre" to "игра"),
        mapOf("id" to 32, "genre" to "ток-шо"),
        mapOf("id" to 33, "genre" to "детский"),
    )

    val countriesList = listOf(
        mapOf("id" to 1, "country" to "США"),
        mapOf("id" to 2, "country" to "Швейцария"),
        mapOf("id" to 3, "country" to "Франция"),
        mapOf("id" to 4, "country" to "Польша"),
        mapOf("id" to 5, "country" to "Великобритания"),
        mapOf("id" to 6, "country" to "Швеция"),
        mapOf("id" to 7, "country" to "Индия"),
        mapOf("id" to 8, "country" to "Испания"),
        mapOf("id" to 9, "country" to "Германия"),
        mapOf("id" to 10, "country" to "Италия"),
        mapOf("id" to 11, "country" to "Гонконг"),
        mapOf("id" to 12, "country" to "Германия (ФРГ)"),
        mapOf("id" to 13, "country" to "Австралия"),
        mapOf("id" to 14, "country" to "Канада"),
        mapOf("id" to 15, "country" to "Мексика"),
        mapOf("id" to 16, "country" to "Япония"),
        mapOf("id" to 17, "country" to "Дания"),
        mapOf("id" to 18, "country" to "Чехия"),
        mapOf("id" to 19, "country" to "Ирландия"),
        mapOf("id" to 20, "country" to "Люксембург"),
        mapOf("id" to 21, "country" to "Китай"),
        mapOf("id" to 22, "country" to "Норвегия"),
        mapOf("id" to 23, "country" to "Нидерланды"),
        mapOf("id" to 24, "country" to "Аргентина"),
        mapOf("id" to 25, "country" to "Финляндия"),
        mapOf("id" to 26, "country" to "Босния и Герцеговина"),
        mapOf("id" to 27, "country" to "Австрия"),
        mapOf("id" to 28, "country" to "Тайвань"),
        mapOf("id" to 29, "country" to "Новая Зеландия"),
        mapOf("id" to 30, "country" to "Бразилия"),
        mapOf("id" to 31, "country" to "Чехословакия"),
        mapOf("id" to 32, "country" to "Мальта"),
        mapOf("id" to 33, "country" to "СССР"),
        mapOf("id" to 34, "country" to "Россия"),
        mapOf("id" to 35, "country" to "Югославия"),
        mapOf("id" to 36, "country" to "Португалия"),
        mapOf("id" to 37, "country" to "Румыния"),
        mapOf("id" to 38, "country" to "Хорватия"),
        mapOf("id" to 39, "country" to "ЮАР"),
        mapOf("id" to 40, "country" to "Куба"),
        mapOf("id" to 41, "country" to "Колумбия"),
        mapOf("id" to 42, "country" to "Израиль"),
        mapOf("id" to 43, "country" to "Намибия"),
        mapOf("id" to 44, "country" to "Турция"),
        mapOf("id" to 45, "country" to "Бельгия"),
        mapOf("id" to 46, "country" to "Сальвадор"),
        mapOf("id" to 47, "country" to "Исландия"),
        mapOf("id" to 48, "country" to "Венгрия"),
        mapOf("id" to 49, "country" to "Корея Южная"),
        mapOf("id" to 50, "country" to "Лихтенштейн"),
        mapOf("id" to 51, "country" to "Болгария"),
        mapOf("id" to 52, "country" to "Филиппины"),
        mapOf("id" to 53, "country" to "Доминикана"),
        mapOf("id" to 55, "country" to "Марокко"),
        mapOf("id" to 56, "country" to "Таиланд"),
        mapOf("id" to 57, "country" to "Кения"),
        mapOf("id" to 58, "country" to "Пакистан"),
        mapOf("id" to 59, "country" to "Иран"),
        mapOf("id" to 60, "country" to "Панама"),
        mapOf("id" to 61, "country" to "Аруба"),
        mapOf("id" to 62, "country" to "Ямайка"),
        mapOf("id" to 63, "country" to "Греция"),
        mapOf("id" to 64, "country" to "Тунис"),
        mapOf("id" to 65, "country" to "Кыргызстан"),
        mapOf("id" to 66, "country" to "Пуэрто Рико"),
        mapOf("id" to 67, "country" to "Казахстан"),
        mapOf("id" to 68, "country" to "Югославия (ФР)"),
        mapOf("id" to 69, "country" to "Алжир"),
        mapOf("id" to 70, "country" to "Германия (ГДР)"),
        mapOf("id" to 71, "country" to "Сингапур"),
        mapOf("id" to 72, "country" to "Словакия"),
        mapOf("id" to 73, "country" to "Афганистан"),
        mapOf("id" to 74, "country" to "Индонезия"),
        mapOf("id" to 75, "country" to "Перу"),
        mapOf("id" to 76, "country" to "Бермуды"),
        mapOf("id" to 77, "country" to "Монако"),
        mapOf("id" to 78, "country" to "Зимбабве"),
        mapOf("id" to 79, "country" to "Вьетнам"),
        mapOf("id" to 80, "country" to "Антильские Острова"),
        mapOf("id" to 81, "country" to "Саудовская Аравия"),
        mapOf("id" to 82, "country" to "Танзания"),
        mapOf("id" to 83, "country" to "Ливия"),
        mapOf("id" to 84, "country" to "Ливан"),
        mapOf("id" to 85, "country" to "Кувейт"),
        mapOf("id" to 86, "country" to "Египет"),
        mapOf("id" to 87, "country" to "Литва"),
        mapOf("id" to 88, "country" to "Венесуэла"),
        mapOf("id" to 89, "country" to "Словения"),
        mapOf("id" to 90, "country" to "Чили"),
        mapOf("id" to 91, "country" to "Багамы"),
        mapOf("id" to 92, "country" to "Эквадор"),
        mapOf("id" to 93, "country" to "Коста-Рика"),
        mapOf("id" to 94, "country" to "Кипр"),
        mapOf("id" to 95, "country" to "Уругвай"),
        mapOf("id" to 96, "country" to "Ирак"),
        mapOf("id" to 97, "country" to "Мартиника"),
        mapOf("id" to 98, "country" to "Эстония"),
        mapOf("id" to 163, "country" to "ОАЭ"),
        mapOf("id" to 100, "country" to "Бангладеш"),
    )

    val countriesMap = countriesList.associateBy({ it["country"] as String }, { it["id"] as Int })
    val genresMap = genresList.associateBy({ it["genre"] as String }, { it["id"] as Int })


    fun getCountryByName(name: String?): Int? {
        return countriesMap[name]
    }

    fun getGenreByName(name: String?): Int? {
        return genresMap[name]
    }
}