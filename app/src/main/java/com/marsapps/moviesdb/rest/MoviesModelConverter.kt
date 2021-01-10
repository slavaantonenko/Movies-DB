package com.marsapps.moviesdb.rest

import com.marsapps.moviesdb.model.Genre
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.response.genre.GenreResponse
import com.marsapps.moviesdb.response.main.MovieResponse

class MoviesModelConverter {

    companion object {
        fun convertMoviesResult(movies: List<MovieResponse?>, genres: List<Genre>): List<Movie> {
            val result = ArrayList<Movie>()

            movies.forEach { movie ->
                movie?.let {
                    result.add(Movie(
                        0,
                        movie.title,
                        getGenresNames(movie.genreIds, genres),
                        movie.releaseDate?.let { it.split("-")[0].toInt() },
                        movie.voteAverage,
                        movie.posterPath
                    ))
                }
            }

            return result
        }

        fun convertGenresResult(genres: List<GenreResponse?>): List<Genre> {
            val result = ArrayList<Genre>()

            genres.forEach { genre ->
                genre?.let {
                    result.add(Genre(
                        it.id,
                        it.name
                    ))
                }
            }

            return result
        }

        private fun getGenresNames(genreIds: List<Int?>?, genres: List<Genre>): String {
            val genreNames = StringBuilder()

            genreIds?.let { ids ->
                ids.forEachIndexed { index, genreID ->
                    genres.filter {
                        genreID !=null && it.id == genreID
                    }.map {
                        it.name?.let { name -> genreNames.append(name) }
                    }

                    if (index + 1 < ids.size)
                        genreNames.append(", ")
                }
            }

            return String(genreNames)
        }
    }
}