package com.marsapps.moviesdb.main.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marsapps.moviesdb.DataManager
import com.marsapps.moviesdb.R
import com.marsapps.moviesdb.model.Genre
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.model.WatchList
import com.marsapps.moviesdb.repository.MoviesRepository

class SplashViewModel(private val dataManager: DataManager, val context: Context) : ViewModel() {

    init {
        initWatchList()
    }

    fun closeConnections() {
        MoviesRepository.getInstance(context).closeConnections()
    }

    private fun initWatchList() {
        val watchLists = MoviesRepository.getInstance(context).getWatchLists()

        if (watchLists.isEmpty()) {
            val watchList = WatchList(0, context.getString(R.string.favourites_watchlist))
            watchLists.add(watchList)
            MoviesRepository.getInstance(context).addWatchList(watchList)
        }

        dataManager.watchListsNames = watchLists
    }

    fun getResults(): MutableLiveData<Boolean> {
        return MoviesRepository.getInstance(context).getResults()
    }

    fun getGenres(): LiveData<List<Genre>>? {
        return MoviesRepository.getInstance(context).getGenresFromDB()
    }

    fun getGenresFromAPI() {
        MoviesRepository.getInstance(context).getGenres()
    }

    fun getMovies(): LiveData<List<Movie>>? {
        return MoviesRepository.getInstance(context).getPopularMoviesFromDB()
    }

    fun getMoviesFromAPI() {
        MoviesRepository.getInstance(context).getPopularMovies(dataManager.genres, dataManager.countryCode)
    }

    fun updateGenres(genres: List<Genre>) {
        dataManager.genres = genres
    }

    fun updateMovies(movies: List<Movie>) {
        dataManager.movies.addAll(movies)
        initWatchLists()
    }

    private fun initWatchLists() {
        val watchLists = mutableListOf<Movie>()

        dataManager.movies.filter {
            it.watchlists.isNotEmpty()
        }.map {
            watchLists.add(it)
        }

        dataManager.watchListsMovies.value = watchLists
    }

    fun updateCountryCode(countryCode: String) {
        dataManager.countryCode = countryCode
    }

    fun resetMovies() {
        MoviesRepository.getInstance(context).resetMovies()
    }
}