package com.example.android_coursework_lvl1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.MovieModel

class FirstDynamicAdapter : PagingDataAdapter<MovieModel,FirstDynamicHolder>(FistDynamicDiffUtilCallback()) {

    private var movieClickListener: FirstOnMovieClickListener? = null



    fun setOnMovieClickListener(listener: FirstOnMovieClickListener) {
        movieClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstDynamicHolder {
        return FirstDynamicHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FirstDynamicHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            filmName.text = movie?.nameRu ?: movie?.nameEn ?: movie?.nameOriginal
            if (movie?.ratingKinopoisk == null) {
                ratingText.text = movie?.ratingImdb.toString()
            } else {
                ratingText.text = movie.ratingKinopoisk.toString()
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


    interface FirstOnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }

}

class FirstDynamicHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

class FistDynamicDiffUtilCallback : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem == newItem

}
