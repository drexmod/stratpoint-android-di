package com.stratpoint.basedesignpatternguide.presentation.popular_movies

import com.stratpoint.basedesignpatternguide.data.database.AppDatabase
import com.stratpoint.basedesignpatternguide.data.network.PopularMoviesService
import com.stratpoint.basedesignpatternguide.data.repository.popular_movies.PopularMoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PopularMoviesModule {
    @Provides
    @Singleton
    fun providePopularMoviesRepository(retrofit: Retrofit, appDatabase: AppDatabase): PopularMoviesRepository = PopularMoviesRepository(
        retrofit.create(PopularMoviesService::class.java),
        appDatabase
    )

    @Provides
    @Singleton
    fun providePopularMoviesAdapter(): PopularMoviesAdapter = PopularMoviesAdapter()
}