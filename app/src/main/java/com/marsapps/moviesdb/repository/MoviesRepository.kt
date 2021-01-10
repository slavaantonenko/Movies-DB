package com.marsapps.moviesdb.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marsapps.moviesdb.Constants.REST.QUERY_KEY_PAGE_DEFAULT
import com.marsapps.moviesdb.Constants.REST.SUCCESSFUL_CODE
import com.marsapps.moviesdb.convertToString
import com.marsapps.moviesdb.db.AppDatabase
import com.marsapps.moviesdb.model.Genre
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.model.WatchList
import com.marsapps.moviesdb.response.genre.MainGenreResponse
import com.marsapps.moviesdb.response.main.MainResponse
import com.marsapps.moviesdb.rest.MoviesModelConverter
import com.marsapps.moviesdb.rest.MoviesService
import com.marsapps.moviesdb.rest.RestClientManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesRepository(context: Context) {

    private var db: AppDatabase = AppDatabase.getInstance(context)
    private var moviesDao = db.movieDao()
    private var genreDao = db.genreDao()
    private var watchListsDao = db.watchListsDao()
    private var result = MutableLiveData(false)
    private var moviesCall: Call<MainResponse>? = null
    private var genresCall: Call<MainGenreResponse>? = null

    companion object {
        private var INSTANCE: MoviesRepository? = null

        fun getInstance(context: Context): MoviesRepository {
            if (INSTANCE == null)
                INSTANCE = MoviesRepository(context)

            return INSTANCE!!
        }
    }

    /**
     * API
     */
    fun getResults(): MutableLiveData<Boolean> {
        return result
    }

    fun getGenres() {
        val moviesService: MoviesService? = RestClientManager.getMoviesServiceInstance()
        genresCall = moviesService?.getGenres()

        genresCall?.enqueue(object : Callback<MainGenreResponse> {
            override fun onResponse(call: Call<MainGenreResponse>, response: Response<MainGenreResponse>) {
                if (response.code() == SUCCESSFUL_CODE && response.body() != null && response.body()!!.genres != null) {
                    genreDao?.insertAll(MoviesModelConverter.convertGenresResult(response.body()!!.genres!!))
                }
                else {
                    result.postValue(true)
                }
            }

            override fun onFailure(call: Call<MainGenreResponse>, t: Throwable) {
                result.postValue(true)
            }
        })
    }

    fun getPopularMovies(genres: List<Genre>, countryCode: String, page: Int = QUERY_KEY_PAGE_DEFAULT) {
        val moviesService: MoviesService? = RestClientManager.getMoviesServiceInstance()
        moviesCall = moviesService?.getPopularMovies(countryCode, page)

        moviesCall?.enqueue(object : Callback<MainResponse> {
            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                if (response.code() == SUCCESSFUL_CODE && response.body() != null && response.body()!!.results != null) {
                    moviesDao?.insertAll(MoviesModelConverter.convertMoviesResult(response.body()!!.results!!, genres))
                }
                else {
                    result.postValue(true)
                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                result.postValue(true)
            }
        })
    }

    /**
     * ROOM Database
     */
    fun getWatchLists(): MutableList<WatchList> {
        watchListsDao?.let {
            return it.getWatchLists().toMutableList()
        }

        return mutableListOf()
    }

    fun addWatchList(list: WatchList) {
        watchListsDao?.insert(list)
    }

    fun getGenresFromDB(): LiveData<List<Genre>>? {
        return genreDao?.getGenres()
    }

    fun getPopularMoviesFromDB(): LiveData<List<Movie>>? {
        return moviesDao?.getMovies()
    }

    fun updateMovieWatchLists(id: Int, watchlists: ArrayList<String>) {
        db.movieDao()?.updateMovieWatchLists(id, watchlists.convertToString())
    }

    fun resetMovies() {
        moviesDao?.deleteAll()
    }

    fun closeConnections() {
        moviesCall?.let {
            if (!it.isCanceled)
                it.cancel()
        }

        genresCall?.let {
            if (!it.isCanceled)
                it.cancel()
        }
    }

    fun closeDB() {
        AppDatabase.close()
    }
}