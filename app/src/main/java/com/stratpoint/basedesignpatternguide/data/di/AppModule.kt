package com.stratpoint.basedesignpatternguide.data.di

import android.app.Application
import androidx.room.Room
import com.stratpoint.basedesignpatternguide.BuildConfig
import com.stratpoint.basedesignpatternguide.data.base.RetrofitBuilder
import com.stratpoint.basedesignpatternguide.data.database.AppDatabase
import com.stratpoint.basedesignpatternguide.data.network.PopularMoviesService
import com.stratpoint.basedesignpatternguide.data.repository.movie_detail.MovieDetailRepository
import com.stratpoint.basedesignpatternguide.data.repository.popular_movies.PopularMoviesRepository
import com.stratpoint.basedesignpatternguide.presentation.popular_movies.PopularMoviesAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val TIMEOUT = 20000 //20 seconds
    private const val DATABASE_NAME = "base_design_pattern_database"

    @Provides
    @Singleton
    fun provideApi(applicationContext: Application): Retrofit =
        Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(BuildConfig.BASE_URL)

            val client = OkHttpClient.Builder().apply {

                connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)

                addInterceptor(HttpLoggingInterceptor().apply {
                    setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
                })

                addInterceptor(RetrofitBuilder.NoInternetInterceptor(applicationContext))

            }.build()

            client(client)
        }.build()

    @Provides
    @Singleton
    fun provideDataBase(applicationContext: Application): AppDatabase =
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

}