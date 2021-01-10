package com.marsapps.moviesdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.marsapps.moviesdb.Constants.PERSISTENCE.DB_NAME
import com.marsapps.moviesdb.model.Genre
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.model.WatchList

@Database(entities = [Genre::class, Movie::class, WatchList::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao?
    abstract fun genreDao(): GenreDao?
    abstract fun watchListsDao(): WatchListsDao?

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return INSTANCE!!
        }

        fun close() {
            if (INSTANCE != null && INSTANCE!!.isOpen) {
                INSTANCE!!.close()
                INSTANCE = null
            }
        }
    }
}