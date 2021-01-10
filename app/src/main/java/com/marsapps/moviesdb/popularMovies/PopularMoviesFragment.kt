package com.marsapps.moviesdb.popularMovies

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.marsapps.moviesdb.R
import com.marsapps.moviesdb.base.BaseFragment
import com.marsapps.moviesdb.impl.OnMovieListener
import com.marsapps.moviesdb.model.Movie
import com.marsapps.moviesdb.popularMovies.adapter.PopularMoviesAdapter
import com.marsapps.moviesdb.popularMovies.viewModel.PopularMoviesViewModel
import kotlinx.android.synthetic.main.dialog_add_to_watchlist.view.*
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

class PopularMoviesFragment : BaseFragment(), OnMovieListener {

    private val job: Job = Job()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val scope: CoroutineScope = CoroutineScope(coroutineContext)

    private val model: PopularMoviesViewModel by viewModel()
    private lateinit var adapter: PopularMoviesAdapter

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PopularFragment.
         */
        @JvmStatic
        fun newInstance() = PopularMoviesFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
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
        rv_movies.layoutManager = LinearLayoutManager(context)
        rv_movies.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter = PopularMoviesAdapter(context!!, model.getMovies().toMutableList(), this)
        rv_movies.adapter = adapter
    }

    override fun initListeners() {
        model.listenToMoviesChanges()?.observe(viewLifecycleOwner, Observer {
            if (it.size > model.getMovies().size) {
                model.addNewMovies(it)
                adapter.updateNewMovies(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.main, menu)

        val searchView = menu.findItem(R.id.action_item_search)?.actionView as SearchView
        searchView.isIconified = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter.filter(s)
                return false
            }
        })
    }

    override fun onMovieAddToWatchList(movie: Movie) {
        showAddToListAlertDialog(movie)
    }

    override fun onMovieRemoveFromWatchList(movie: Movie) {}

    override fun onMovieClick(title: String) {
        openYoutube(title)
    }

    override fun onMovieScrollListener() {
        scope.launch {
            model.getMoviesFromAPI()
        }
    }

    private fun showAddToListAlertDialog(movie: Movie) {
        val watchLists = model.getPossibleWatchLists(movie)

        if (watchLists.size > 0) {
            var watchList = watchLists[0]
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_to_watchlist, null)

            val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme)
            builder.setView(dialogView)
            builder.setTitle(movie.title)
            builder.setMessage(getString(R.string.choose_watchlist))

            val spinnerAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_dropdown_item)
            spinnerAdapter.addAll(watchLists)
            dialogView.sp_possible_watchlists.adapter = spinnerAdapter

            dialogView.sp_possible_watchlists.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    (parent?.getItemAtPosition(position) as String).apply {
                        if (watchList != this)
                            watchList = this
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            builder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                scope.launch {
                    model.addMovieToWatchList(movie, watchList)
                }

            }

            builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()

            listOf(AlertDialog.BUTTON_POSITIVE, AlertDialog.BUTTON_NEGATIVE).forEach {
                alertDialog.getButton(it).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
        }
        else {
            Toast.makeText(requireContext(), getString(R.string.no_watchlist_available), Toast.LENGTH_LONG).show()
        }
    }
}