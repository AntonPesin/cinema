package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.MovieModel

class PopularAdapter: PagingDataAdapter<MovieModel, PopularViewHolder>(PopularDiffUtilCallback()) {


    private var movieClickListener: PopularOnMovieClickListener? = null


    fun setOnMovieClickListener(listener: PopularOnMovieClickListener) {
        movieClickListener = listener
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            filmName.text = movie?.nameRu ?: movie?.nameEn
            genre.text = movie?.genres?.joinToString(", ") { it.genre }
            ratingText.text = movie?.rating.toString()
            poster.setOnClickListener {
                movieClickListener?.onMovieClick(movie)
            }
            movie.let {
                Glide
                    .with(poster.context)
                    .load(it?.posterUrl)
                    .override(111, 156)
                    .into(poster)
            }
        }
    }


    interface PopularOnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }
}


class PopularViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

class  PopularDiffUtilCallback : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem.filmId == newItem.filmId

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem == newItem
}