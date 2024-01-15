package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.MovieModel
class SeenAdapter (private var movies: List<MovieModel>) : RecyclerView.Adapter<SeenViewHolder>() {

    private var movieClickListener: OnMovieClickListener? = null
    private var clearClickListener: OnClearClickListener? = null
    private var showFooter = false

    fun updateData(newMovies: List<MovieModel>) {
        movies = newMovies
        notifyDataSetChanged()
    }
    fun setOnMovieClickListener(listener: OnMovieClickListener) {
        movieClickListener = listener
    }

    fun setOnClearClickListener(listener: OnClearClickListener) {
        clearClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeenViewHolder {
        return SeenViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SeenViewHolder, position: Int) {
        if (position < movies.size) {
            val movie = movies[position]
            with(holder.binding) {
                filmName.text = movie.nameRu ?: movie.nameEn
                if ((movie.ratingKinopoisk ?: movie.ratingImdb) == null) {
                    holder.binding.frameRating.visibility = View.INVISIBLE
                } else {
                    ratingText.text = movie.ratingKinopoisk ?: movie.ratingImdb
                }
                poster.setOnClickListener {
                    movieClickListener?.onMovieClick(movie)
                }
                movie.let {
                    Glide
                        .with(poster.context)
                        .load(it.posterUrlPreview)
                        .override(111, 156)
                        .into(poster)
                }
            }
        } else {
            holder.binding.clear.visibility = if (showFooter) View.VISIBLE else View.GONE
            holder.binding.frameRating.visibility = View.GONE
            holder.binding.clear.setOnClickListener{
                clearClickListener?.onClearClick()
            }
        }

    }
    fun showFooter(show: Boolean) {
        showFooter = show && movies.isNotEmpty()
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return movies.size + 1
    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }

    interface OnClearClickListener {
        fun onClearClick()
    }

}
class SeenViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)