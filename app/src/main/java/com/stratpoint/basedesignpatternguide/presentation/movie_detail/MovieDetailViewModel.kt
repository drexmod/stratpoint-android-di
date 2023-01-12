package com.stratpoint.basedesignpatternguide.presentation.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stratpoint.basedesignpatternguide.data.base.Status
import com.stratpoint.basedesignpatternguide.data.repository.movie_detail.MovieDetailRepository
import com.stratpoint.basedesignpatternguide.domain.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository
): ViewModel() {

    companion object {
        const val ERROR_NO_ID = "Movie not found. Please try again."
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getMovie(id: String?) {

        if (id != null) {

            viewModelScope.launch {

                val result = repository.getMovieDetail(id)

                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let {
                            _movie.value = it
                        }
                    }
                    Status.ERROR -> {
                        result.message?.let {
                            _errorMessage.value = it
                        }
                    }
                }

            }

        }
        else {
            _errorMessage.value = ERROR_NO_ID
        }

    }

}