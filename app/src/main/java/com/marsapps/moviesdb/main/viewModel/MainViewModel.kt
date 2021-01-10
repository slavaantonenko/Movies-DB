package com.marsapps.moviesdb.main.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.marsapps.moviesdb.DataManager
import com.marsapps.moviesdb.model.WatchList
import com.marsapps.moviesdb.repository.MoviesRepository

class MainViewModel(private val dataManager: DataManager, val context: Context) : ViewModel() {

    fun addCustomWatchlist(name: String): Boolean {
        if (name.isNotEmpty()) {
            val contains = dataManager.watchListsNames.any {
                it.name == name
            }

            if (!contains) {
                val watchList = WatchList(0, name)
                dataManager.watchListsNames.add(watchList)
                MoviesRepository.getInstance(context).addWatchList(watchList)
                return true
            }
        }

        return false
    }

    fun closeConnection() {
        MoviesRepository.getInstance(context).closeConnections()
        MoviesRepository.getInstance(context).closeDB()
    }
}