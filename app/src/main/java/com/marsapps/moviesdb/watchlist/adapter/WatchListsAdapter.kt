package com.marsapps.moviesdb.watchlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marsapps.moviesdb.Constants.REST.BASE_IMAGES_URL
import com.marsapps.moviesdb.R
import com.marsapps.moviesdb.impl.OnMovieListener
import com.marsapps.moviesdb.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class WatchListsAdapter(private val context: Context, private var movies: MutableList<Movie>, val onMovieListener: OnMovieListener)
    : RecyclerView.Adapter<WatchListsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val image = view.iv_movie
        private val title = view.tv_title
        private val genre = view.tv_genre
        private val releaseYear = view.tv_release_year
        private val rating = view.tv_rating
        private val addToList = view.iv_add_to_watchlist
        private val deleteFromList = view.iv_delete_from_watchlist

        init {
            view.setOnClickListener(this)
            deleteFromList.setOnClickListener(this)
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

            addToList.visibility = View.GONE
            deleteFromList.visibility = View.VISIBLE
        }

        override fun onClick(view: View?) {
            when (view?.id) {
                R.id.iv_delete_from_watchlist -> onMovieListener.onMovieRemoveFromWatchList(getItem(adapterPosition))
                else -> {
                    getItem(adapterPosition).title?.let {
                        onMovieListener.onMovieClick(it)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListsAdapter.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WatchListsAdapter.ViewHolder, position: Int) {
        holder.onBindViewHolder(movies[position])
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

    fun changeWatchList(movies: MutableList<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }
}