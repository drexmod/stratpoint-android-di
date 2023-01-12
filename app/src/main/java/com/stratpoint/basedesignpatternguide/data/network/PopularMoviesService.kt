package com.stratpoint.basedesignpatternguide.data.network

import com.stratpoint.basedesignpatternguide.BuildConfig
import com.stratpoint.basedesignpatternguide.constant.AppConstants
import com.stratpoint.basedesignpatternguide.data.model.response.PopularMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularMoviesService {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String = AppConstants.API_LANGUAGE,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): PopularMovieResponse

}