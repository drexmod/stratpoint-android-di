package com.stratpoint.basedesignpatternguide.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stratpoint.basedesignpatternguide.domain.Movie

@Dao
abstract class MovieDao {

    @Query("SELECT * FROM movie")
    abstract suspend fun getAllMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movie")
    abstract suspend fun deleteAllMovies()

    @Query("SELECT * FROM movie WHERE id=:id")
    abstract suspend fun getMovie(id: String): Movie

}
