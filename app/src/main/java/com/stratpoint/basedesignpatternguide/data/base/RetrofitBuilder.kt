package com.stratpoint.basedesignpatternguide.data.base

import android.content.Context
import com.stratpoint.basedesignpatternguide.util.NetworkUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private lateinit var appContext: Context
    private lateinit var BASE_URL: String
    private var isDebug = true
    private const val TIMEOUT = 20000//20 seconds

    val retrofit: Retrofit by lazy { createRetrofitInstance() }

    fun initialize(context: Context, baseUrl: String, isDebug: Boolean) {
        appContext = context
        BASE_URL = baseUrl
        RetrofitBuilder.isDebug = isDebug
    }

    fun createRetrofitInstance(): Retrofit = Retrofit.Builder().apply {
        addConverterFactory(GsonConverterFactory.create())
        baseUrl(BASE_URL)

        val client = OkHttpClient.Builder().apply {

            connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)

            addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
            })

            addInterceptor(NoInternetInterceptor(appContext))

        }.build()

        client(client)
    }.build()

    class NoInternetInterceptor(private val context: Context) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            return if (!NetworkUtil.isNetworkAvailable(context)) {
                throw NoInternetException()
            } else {
                val builder = chain.request().newBuilder()
                chain.proceed(builder.build())
            }
        }

        inner class NoInternetException() : IOException("No internet connection")

    }

}