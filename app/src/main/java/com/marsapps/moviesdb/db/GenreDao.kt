package com.marsapps.moviesdb.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marsapps.moviesdb.Constants.PERSISTENCE.GENRES_TABLE
import com.marsapps.moviesdb.model.Genre

@Dao
interface GenreDao {
    @Query("SELECT * from $GENRES_TABLE")
    fun getGenres(): LiveData<List<Genre>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Genre>)
}