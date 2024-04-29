package com.inter.network

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkUtils {
    val logginInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {

        }

    }).setLevel(HttpLoggingInterceptor.Level.BODY)

    val interceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originRequest = chain.request()
            val newUrl = originRequest.url.newBuilder().build()
            val newRequest = originRequest.newBuilder().url(newUrl).build()
            return chain.proceed(newRequest)
        }

    }
    val okHttpClient = OkHttpClient.Builder().apply {
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
        connectTimeout(60, TimeUnit.SECONDS)
        addInterceptor(interceptor)
        addInterceptor(logginInterceptor)
        retryOnConnectionFailure(false)
    }.build()
    val BASE_URL = "https://onestep-4004e-default-rtdb.asia-southeast1.firebasedatabase.app"
    val GSON = GsonBuilder().setLenient().create()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GSON)).client(okHttpClient).build()

    }
}