package com.marsapps.moviesdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marsapps.moviesdb.Constants.PERSISTENCE.WATCHLISTS_TABLE

@Entity(tableName = WATCHLISTS_TABLE)
data class WatchList(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = ""
)