package com.marsapps.moviesdb.rest

import com.marsapps.moviesdb.Constants.REST.BASE_API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClientManager {

    companion object {
        private var moviesService: MoviesService? = null

        fun getMoviesServiceInstance(): MoviesService? {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            moviesService = retrofit.create(MoviesService::class.java)
            return moviesService
        }
    }
}