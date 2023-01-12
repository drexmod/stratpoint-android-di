package com.stratpoint.basedesignpatternguide.data.repository.popular_movies

import com.stratpoint.basedesignpatternguide.data.base.BaseRepository
import com.stratpoint.basedesignpatternguide.data.base.Resource
import com.stratpoint.basedesignpatternguide.data.database.AppDatabase
import com.stratpoint.basedesignpatternguide.data.network.PopularMoviesService
import com.stratpoint.basedesignpatternguide.domain.Movie
import javax.inject.Inject

class PopularMoviesRepository @Inject constructor(
    private val service: PopularMoviesService,
    private val appDatabase: AppDatabase
): BaseRepository() {

    suspend fun getPopularMovies(page: Int): Resource<List<Movie>> = serviceCall {

        val result = service.getPopularMovies(page)

        if (page == 1) {
            appDatabase.movieDao().deleteAllMovies()
        }

        appDatabase.movieDao().insertMovies(result.results)

        result.results

    }

    suspend fun getCachedPopularMovies(): Resource<List<Movie>> = serviceCall {
        appDatabase.movieDao().getAllMovies()
    }

}