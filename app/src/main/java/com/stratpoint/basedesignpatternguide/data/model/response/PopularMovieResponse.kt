package com.stratpoint.basedesignpatternguide.data.model.response

import com.google.gson.annotations.SerializedName
import com.stratpoint.basedesignpatternguide.domain.Movie

data class PopularMovieResponse(
    @SerializedName("page") var page: Int,
    @SerializedName("results") var results: List<Movie>,
    @SerializedName("total_pages") var totalPages: Int,
    @SerializedName("total_results") var totalResults: Int,
)
