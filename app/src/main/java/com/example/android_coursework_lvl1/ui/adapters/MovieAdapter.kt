package com.example.android_coursework_lvl1.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel

class MovieAdapter : PagingDataAdapter<MovieModel, MovieViewHolder>(MovieDiffUtilCallback()) {

    private var movieClickListener: OnMovieClickListener? = null

    fun setOnMovieClickListener(listener: OnMovieClickListener) {
        movieClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            filmName.text = movie?.nameRu ?: movie?.nameEn ?: movie?.nameOriginal
            val rating = movie?.ratingKinopoisk ?: movie?.ratingImdb
            if (rating == null) {
                frameRating.visibility = View.INVISIBLE
            } else {
                ratingText.text = rating
            }
            genre.text = movie?.genres?.joinToString(", ") { it.genre }
            poster.setOnClickListener {
                movieClickListener?.onMovieClick(movie)
            }
            movie.let {
                Glide
                    .with(poster.context)
                    .load(it?.posterUrlPreview)
                    .override(111, 156)
                    .into(poster)
            }
        }
    }


    interface OnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }

}

class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

class MovieDiffUtilCallback : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem == newItem

}