package com.marsapps.moviesdb.impl

import com.marsapps.moviesdb.model.Movie

interface OnMovieListener {
    fun onMovieAddToWatchList(movie: Movie)
    fun onMovieRemoveFromWatchList(movie: Movie)
    fun onMovieClick(title: String)
    fun onMovieScrollListener()
}