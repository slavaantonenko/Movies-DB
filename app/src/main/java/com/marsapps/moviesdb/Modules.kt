package com.marsapps.moviesdb

import com.marsapps.moviesdb.main.viewModel.MainViewModel
import com.marsapps.moviesdb.main.viewModel.SplashViewModel
import com.marsapps.moviesdb.popularMovies.viewModel.PopularMoviesViewModel
import com.marsapps.moviesdb.watchlist.viewModel.WatchListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataManagerModule = module { single { DataManager() } }

val viewModelModule = module {
    viewModel {
        SplashViewModel(get(), get())
    }

    viewModel {
        MainViewModel(get(), get())
    }

    viewModel {
        PopularMoviesViewModel(get(), get())
    }

    viewModel {
        WatchListsViewModel(get(), get())
    }
}