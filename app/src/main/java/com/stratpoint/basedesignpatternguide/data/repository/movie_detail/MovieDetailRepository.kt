package com.stratpoint.basedesignpatternguide.data.repository.movie_detail

import com.stratpoint.basedesignpatternguide.data.base.BaseRepository
import com.stratpoint.basedesignpatternguide.data.base.Resource
import com.stratpoint.basedesignpatternguide.data.database.AppDatabase
import com.stratpoint.basedesignpatternguide.domain.Movie
import javax.inject.Inject

class MovieDetailRepository @Inject constructor(
    private val appDatabase: AppDatabase
) : BaseRepository() {

    suspend fun getMovieDetail(id: String): Resource<Movie> = serviceCall {
        appDatabase.movieDao().getMovie(id)
    }

}