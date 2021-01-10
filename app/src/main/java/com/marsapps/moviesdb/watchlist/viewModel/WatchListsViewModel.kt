package com.marsapps.moviesdb.watchlist.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marsapps.moviesdb.DataManager
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.repository.MoviesRepository

class WatchListsViewModel(private val dataManager: DataManager, val context: Context) : ViewModel() {

    fun listenToWatchlists(): MutableLiveData<MutableList<Movie>> {
        return dataManager.watchListsMovies
    }

    fun getWatchListsNames(): ArrayList<String> {
        val watchlists = arrayListOf<String>()

        dataManager.watchListsNames.forEach {
            watchlists.add(it.name)
        }

        return watchlists
    }

    fun getMovies(list: String): MutableList<Movie> {
        val watchList = mutableListOf<Movie>()

        dataManager.watchListsMovies.value?.let { movies ->
            movies.filter { movie ->
                movie.watchlists.contains(list)
            }.map {
                watchList.add(it)
            }
        }

        return watchList
    }

    fun removeMovieFromWatchList(movie: Movie, currentList: String) {
        movie.watchlists.removeIf {
            it == currentList
        }

        if (movie.watchlists.size > 0)
            MoviesRepository.getInstance(context).updateMovieWatchLists(movie.id, movie.watchlists)
        else
            MoviesRepository.getInstance(context).updateMovieWatchLists(movie.id, arrayListOf(""))

        dataManager.watchListsMovies.value?.let { movies ->

            movies.filter {
                it.id == movie.id
            }.map {
                it.watchlists.remove(currentList)
            }

            dataManager.watchListsMovies.postValue(dataManager.watchListsMovies.value)
        }
    }
}