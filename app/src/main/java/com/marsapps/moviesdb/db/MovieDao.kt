package com.marsapps.moviesdb.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marsapps.moviesdb.Constants.PERSISTENCE.MOVIES_TABLE
import com.marsapps.moviesdb.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * from $MOVIES_TABLE")
    fun getMovies(): LiveData<List<Movie>>

    @Query("UPDATE $MOVIES_TABLE SET watchlists = :watchlists WHERE id = :id")
    fun updateMovieWatchLists(id: Int, watchlists: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>)

    @Query("DELETE FROM $MOVIES_TABLE")
    fun deleteAll()
}