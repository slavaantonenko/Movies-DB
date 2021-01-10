package com.marsapps.moviesdb.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.marsapps.moviesdb.R
import com.marsapps.moviesdb.base.BaseFragment
import com.marsapps.moviesdb.impl.OnMovieListener
import com.marsapps.moviesdb.impl.OnWatchListListener
import com.marsapps.moviesdb.main.MainActivity
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.watchlist.adapter.WatchListsAdapter
import com.marsapps.moviesdb.watchlist.viewModel.WatchListsViewModel
import kotlinx.android.synthetic.main.fragment_watchlists.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class WatchListsFragment : BaseFragment(), AdapterView.OnItemSelectedListener, OnMovieListener, OnWatchListListener {

    private val job: Job = Job()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val scope: CoroutineScope = CoroutineScope(coroutineContext)

    private val model: WatchListsViewModel by viewModel()
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var adapter: WatchListsAdapter
    private var currentList = ""

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment WatchListsFragment.
         */
        @JvmStatic
        fun newInstance(): WatchListsFragment {
            return WatchListsFragment().apply {
                arguments = Bundle().apply {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_watchlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListeners()
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

    override fun initView() {
        currentList = getString(R.string.favourites_watchlist)

        spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapter.addAll(model.getWatchListsNames())
        sp_watchlists.adapter = spinnerAdapter

        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_watchlist.layoutManager = LinearLayoutManager(requireContext())
        rv_watchlist.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter = WatchListsAdapter(requireContext(), model.getMovies(currentList), this)
        rv_watchlist.adapter = adapter
    }

    override fun initListeners() {
        (requireActivity() as MainActivity).setOnWatchListListener(this)
        sp_watchlists.onItemSelectedListener = this

        model.listenToWatchlists().observe(viewLifecycleOwner, Observer {
            adapter.changeWatchList(model.getMovies(currentList))
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        (parent?.getItemAtPosition(position) as String).apply {
            if (currentList != this) {
                currentList = this
                adapter.changeWatchList(model.getMovies(currentList))
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onMovieAddToWatchList(movie: Movie) {}

    override fun onMovieRemoveFromWatchList(movie: Movie) {
        scope.launch {
            model.removeMovieFromWatchList(movie, currentList)
        }
    }

    override fun onMovieClick(title: String) {
        openYoutube(title)
    }

    override fun onMovieScrollListener() {}

    override fun onWatchListAdded(name: String) {
        spinnerAdapter.add(name)
    }
}