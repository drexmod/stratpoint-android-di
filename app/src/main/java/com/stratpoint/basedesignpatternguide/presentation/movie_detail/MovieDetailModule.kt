package com.stratpoint.basedesignpatternguide.presentation.movie_detail

import com.stratpoint.basedesignpatternguide.data.database.AppDatabase
import com.stratpoint.basedesignpatternguide.data.repository.movie_detail.MovieDetailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDetailModule {
    @Provides
    @Singleton
    fun provideMovieDetailRepository(appDatabase: AppDatabase): MovieDetailRepository = MovieDetailRepository(
        appDatabase
    )
}