package com.marsapps.moviesdb.popularMovies.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marsapps.moviesdb.Constants.REST.BASE_IMAGES_URL
import com.marsapps.moviesdb.Constants.REST.MINIMUM_VISIBLE_MOVIES_LEFT
import com.marsapps.moviesdb.Constants.REST.MOVIES_PER_PAGE
import com.marsapps.moviesdb.R
import com.marsapps.moviesdb.impl.OnMovieListener
import com.marsapps.moviesdb.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*
import java.util.*

class PopularMoviesAdapter(private val context: Context, private var movies: MutableList<Movie>, val onMovieListener: OnMovieListener)
    : RecyclerView.Adapter<PopularMoviesAdapter.ViewHolder>() {

    private var fetchingMovies = false
    private val originList: MutableList<Movie> = movies.map{ it.copy() }.toMutableList() // Used for filtering

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val image = view.iv_movie
        private val title = view.tv_title
        private val genre = view.tv_genre
        private val releaseYear = view.tv_release_year
        private val rating = view.tv_rating
        private val addToList = view.iv_add_to_watchlist

        init {
            view.setOnClickListener(this)
            addToList.setOnClickListener(this)
        }

        fun onBindViewHolder(movie: Movie) {
            setMovieImage(image, movie.imageID)
            title.text = movie.title
            genre.text = movie.genres

            movie.releaseYear?.let {
                releaseYear.text = it.toString()
            }

            movie.rating?.let {
                rating.text = it.toString()
            }
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.iv_add_to_watchlist -> onMovieListener.onMovieAddToWatchList(getItem(adapterPosition))
                else -> {
                    getItem(adapterPosition).title?.let {
                        onMovieListener.onMovieClick(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesAdapter.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PopularMoviesAdapter.ViewHolder, position: Int) {
        holder.onBindViewHolder(movies[position])

        // Load next page from API before scrolling to the end of the list
        if (movies.size - position == MINIMUM_VISIBLE_MOVIES_LEFT && !fetchingMovies) {
            fetchingMovies = true
            onMovieListener.onMovieScrollListener()
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    private fun getItem(position: Int): Movie {
        return movies[position]
    }

    private fun setMovieImage(imageView: ImageView, id: String?) {
        id?.let {
            Glide.with(context)
                .load("$BASE_IMAGES_URL$id")
                .error(R.drawable.logo)
                .into(imageView)
        }
    }

    fun updateNewMovies(newMovies: List<Movie>) {
        fetchingMovies = false

        movies.addAll(newMovies.subList(newMovies.size - MOVIES_PER_PAGE, newMovies.size))
        notifyItemRangeChanged(movies.size - MOVIES_PER_PAGE, MOVIES_PER_PAGE)
        originList.addAll(movies.subList(movies.size - MOVIES_PER_PAGE, movies.size))
    }

    // Filter movies list according to searched text.
    fun filter(str: String) {
        movies.clear()
        movies.addAll(originList.filter {
            if (it.title != null)
                it.title!!.toLowerCase(Locale.getDefault()).startsWith(str)
            else
                true
        })
        notifyDataSetChanged()
    }
}