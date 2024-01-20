package com.example.android_coursework_lvl1.ui.limitedAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel

class LimitedFirstDynamicAdapter : RecyclerView.Adapter<LimitedFirstDynamicViewHolder>() {

    private var movies: List<MovieModel> = emptyList()
    private var movieClickListener: OnMovieClickListener? = null

    fun setOnMovieClickListener(listener: OnMovieClickListener) {
        movieClickListener = listener
    }

    fun setLimitedData(movies: List<MovieModel>) {
        this.movies = movies.take(20)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LimitedFirstDynamicViewHolder {
        return LimitedFirstDynamicViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LimitedFirstDynamicViewHolder, position: Int) {
        val movie = movies[position]
        with(holder.binding) {
            filmName.text = movie.nameRu ?: movie.nameEn
            val rating = movie.ratingKinopoisk ?: movie.ratingImdb
            if (rating == null) {
                holder.binding.frameRating.visibility = View.INVISIBLE
            } else {
                ratingText.text = rating
            }
            genre.text = movie.genres!!.joinToString(", ") { it.genre }
            poster.setOnClickListener {
                movieClickListener?.onMovieClick(movie)
            }
            movie.let {
                Glide
                    .with(poster.context)
                    .load(it.posterUrl)
                    .override(111, 156)
                    .into(poster)
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }
}

class LimitedFirstDynamicViewHolder(val binding: MovieItemBinding) :
    RecyclerView.ViewHolder(binding.root)