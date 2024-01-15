package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.SearchMovieModel

class SearchAdapter : PagingDataAdapter<SearchMovieModel, SearchViewHolder>(SearchDiffUtilCallback()) {

    private var movieClickListener: OnMovieClickListener? = null

    fun setOnMovieClickListener(listener: OnMovieClickListener) {
        movieClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            filmName.text = movie?.nameRu ?: movie?.nameEn ?: movie?.nameOriginal
            val rating = movie?.ratingKinopoisk ?: movie?.ratingImdb
            if (rating == null) {
                frameRating.visibility = View.INVISIBLE
            } else {
                ratingText.text = rating.toString()
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
        fun onMovieClick(movie: SearchMovieModel?)
    }

}

class SearchViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

class SearchDiffUtilCallback : DiffUtil.ItemCallback<SearchMovieModel>() {
    override fun areItemsTheSame(oldItem: SearchMovieModel, newItem: SearchMovieModel): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: SearchMovieModel, newItem: SearchMovieModel): Boolean =
        oldItem == newItem

}