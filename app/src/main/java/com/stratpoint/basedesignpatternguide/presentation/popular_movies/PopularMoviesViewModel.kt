package com.stratpoint.basedesignpatternguide.presentation.popular_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stratpoint.basedesignpatternguide.constant.PaginationLoadingIndicator
import com.stratpoint.basedesignpatternguide.data.base.Status
import com.stratpoint.basedesignpatternguide.data.repository.popular_movies.PopularMoviesRepository
import com.stratpoint.basedesignpatternguide.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val repository: PopularMoviesRepository
): ViewModel() {

    private val _loadingState = MutableStateFlow<PaginationLoadingIndicator>(PaginationLoadingIndicator.NONE)
    val loadingState: StateFlow<PaginationLoadingIndicator> = _loadingState.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessage: SharedFlow<String?> = _errorMessage.asSharedFlow()

    private val movieList = mutableListOf<Movie>()
    private val _movies = MutableSharedFlow<MutableList<Movie>>()
    val movies: SharedFlow<List<Movie>> = _movies.asSharedFlow()

    private var currentPage = 0

    fun getMovies(resetPage: Boolean = false) {

        if (_loadingState.value != PaginationLoadingIndicator.NONE) {
            return
        }

        _loadingState.value = if (resetPage) PaginationLoadingIndicator.SWIPE_REFRESH else PaginationLoadingIndicator.MORE

        viewModelScope.launch {

            // Load cache on first load
            if (movieList.isEmpty()) {
                val cache = repository.getCachedPopularMovies()

                when (cache.status) {
                    Status.SUCCESS -> {
                        cache.data?.let { cachedMovies ->
                            _movies.emit(cachedMovies.toMutableList())
                        }
                    }
                    Status.ERROR -> {
                        cache.message?.let {
                            _errorMessage.emit(it)
                        }
                    }
                }

            }

            //Resolve bug if there was an error upon reset
            if (resetPage) {
                currentPage = 0
            }

            val result = repository.getPopularMovies(currentPage + 1)

            when (result.status) {
                Status.SUCCESS -> result.data?.let {

                    if (resetPage) {
                        movieList.clear()
                    }

                    movieList.addAll(it)
                    currentPage += 1
                    _movies.emit(movieList)

                }
                Status.ERROR -> {
                    result.message?.let { _errorMessage.emit(it) }
                }
            }

            _loadingState.value = PaginationLoadingIndicator.NONE

        }

    }

}