package com.stratpoint.basedesignpatternguide.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    var id: String = "",
    @SerializedName("title") var title: String,
    @SerializedName("overview") var overview: String,
    @SerializedName("release_date") var releaseDate: String?,
    @SerializedName("vote_average") var ratings: Double,
    @SerializedName("poster_path") var posterPath: String?,
    @SerializedName("backdrop_path") var backdropPath: String?
): Serializable
