package com.marsapps.moviesdb.pagerAdapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.marsapps.moviesdb.R
import com.marsapps.moviesdb.popularMovies.PopularMoviesFragment
import com.marsapps.moviesdb.watchlist.WatchListsFragment

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val tabTitles = arrayOf(R.string.tab_popular_movies, R.string.tab_watchlists)

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> PopularMoviesFragment.newInstance()
                else -> WatchListsFragment.newInstance()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return context.resources.getString(tabTitles[position])
        }

        override fun getCount(): Int {
            return 2
        }
}