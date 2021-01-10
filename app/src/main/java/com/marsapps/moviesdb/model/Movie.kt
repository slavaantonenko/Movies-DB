package com.marsapps.moviesdb.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.marsapps.moviesdb.Constants.PERSISTENCE.MOVIES_TABLE
import com.marsapps.moviesdb.db.Converters

@Entity(tableName = MOVIES_TABLE)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String? = null,
    var genres: String? = null,
    var releaseYear: Int? = null,
    var rating: Double? = null,
    var imageID: String? = null,
    @TypeConverters(Converters::class) var watchlists: ArrayList<String> = arrayListOf()
)