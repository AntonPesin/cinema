package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.MovieModel

class Top250Adapter : PagingDataAdapter<MovieModel, Top250ViewHolder>(Top250DiffUtilCallback()) {

    private var movieClickListener: Top250OnMovieClickListener? = null

    fun setOnMovieClickListener(listener: Top250OnMovieClickListener) {
        movieClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Top250ViewHolder {
        return Top250ViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Top250ViewHolder, position: Int) {
        val movie = getItem(position)
        holder.binding.filmName.text = movie?.nameRu ?: movie?.nameEn
        holder.binding.ratingText.text = movie?.rating.toString()
        holder.binding.genre.text = movie?.genres?.joinToString(", ") { it.genre }
        holder.binding.poster.setOnClickListener {
            movieClickListener?.onMovieClick(movie)
        }
        movie.let {
            Glide
                .with(holder.binding.poster.context)
                .load(it?.posterUrl)
                .override(111, 156)
                .into(holder.binding.poster)
        }
    }

    interface Top250OnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }
}


class Top250ViewHolder(val binding: MovieItemBinding) :
    RecyclerView.ViewHolder(binding.root)

class Top250DiffUtilCallback : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem.filmId == newItem.filmId

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem == newItem

}





