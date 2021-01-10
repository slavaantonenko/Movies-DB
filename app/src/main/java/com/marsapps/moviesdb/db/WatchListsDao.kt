package com.marsapps.moviesdb.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.marsapps.moviesdb.Constants.PERSISTENCE.WATCHLISTS_TABLE
import com.marsapps.moviesdb.model.WatchList

@Dao
interface WatchListsDao {
    @Query("SELECT * from $WATCHLISTS_TABLE")
    fun getWatchLists(): List<WatchList>

    @Insert
    fun insert(list: WatchList)
}
