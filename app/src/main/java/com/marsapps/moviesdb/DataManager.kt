package com.marsapps.moviesdb

import androidx.lifecycle.MutableLiveData
import com.marsapps.moviesdb.Constants.REST.QUERY_KEY_REGION_DEFAULT
import com.marsapps.moviesdb.model.Genre
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.model.WatchList

class DataManager {

    var movies: MutableList<Movie> = mutableListOf()
    var genres: List<Genre> = listOf()
    var watchListsMovies: MutableLiveData<MutableList<Movie>> = MutableLiveData(mutableListOf())
    var watchListsNames: MutableList<WatchList> = mutableListOf()
    var countryCode: String = QUERY_KEY_REGION_DEFAULT
}