package com.marsapps.moviesdb.popularMovies.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.marsapps.moviesdb.Constants.REST.MOVIES_PER_PAGE
import com.marsapps.moviesdb.DataManager
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.repository.MoviesRepository

class PopularMoviesViewModel(private val dataManager: DataManager, val context: Context) : ViewModel() {

    fun listenToMoviesChanges(): LiveData<List<Movie>>? {
        return MoviesRepository.getInstance(context).getPopularMoviesFromDB()
    }

    fun getMovies(): MutableList<Movie> {
        return dataManager.movies
    }

    fun getMoviesFromAPI() {
        val nextPage = dataManager.movies.size / MOVIES_PER_PAGE + 1
        MoviesRepository.getInstance(context).getPopularMovies(dataManager.genres, dataManager.countryCode, nextPage)
    }

    fun getPossibleWatchLists(movie: Movie): ArrayList<String> {
        val possibleWatchLists = arrayListOf<String>()

        dataManager.watchListsNames.filter {
            !movie.watchlists.contains(it.name)
        }.map {
            possibleWatchLists.add(it.name)
        }

        return possibleWatchLists
    }

    /**
     * Update DataManager movies list with new movies loaded from latest page.
     */
    fun addNewMovies(movies: List<Movie>) {
        dataManager.movies.addAll(movies.subList(movies.size - MOVIES_PER_PAGE, movies.size))
    }

    fun addMovieToWatchList(movie: Movie, watchlist: String) {
        movie.watchlists.add(watchlist)
        MoviesRepository.getInstance(context).updateMovieWatchLists(movie.id, movie.watchlists)

        dataManager.watchListsMovies.value?.let { movies ->
            val empty = movies.filter {
                it.id == movie.id
            }.map {
                it.watchlists.add(watchlist)
            }.isEmpty()

            if (empty) // If movie is not already in watchlist we just add it.
                movies.add(movie)

            dataManager.watchListsMovies.postValue(movies)
        }
    }
}