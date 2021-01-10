package com.marsapps.moviesdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marsapps.moviesdb.Constants.PERSISTENCE.GENRES_TABLE

@Entity(tableName = GENRES_TABLE)
data class Genre(
    @PrimaryKey
    val id: Int? = null,
    val name: String? = null
)