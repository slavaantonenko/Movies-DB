package com.marsapps.moviesdb.rest

import com.marsapps.moviesdb.Constants.REST.GENRES
import com.marsapps.moviesdb.Constants.REST.POPULAR
import com.marsapps.moviesdb.Constants.REST.QUERY_KEY_PAGE
import com.marsapps.moviesdb.Constants.REST.QUERY_KEY_PAGE_DEFAULT
import com.marsapps.moviesdb.Constants.REST.QUERY_KEY_REGION
import com.marsapps.moviesdb.response.genre.MainGenreResponse
import com.marsapps.moviesdb.response.main.MainResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    @GET(POPULAR)
    fun getPopularMovies(@Query(QUERY_KEY_REGION) region: String,
                         @Query(QUERY_KEY_PAGE) page: Int = QUERY_KEY_PAGE_DEFAULT): Call<MainResponse>

    @GET(GENRES)
    fun getGenres(): Call<MainGenreResponse>
}