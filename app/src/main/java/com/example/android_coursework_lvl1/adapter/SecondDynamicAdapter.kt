package com.example.android_coursework_lvl1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.MovieModel

class SecondDynamicAdapter : PagingDataAdapter<MovieModel, SecondDynamicHolder>(SecondDynamicDiffUtilCallBack()) {


    private var movieClickListener: SecondOnMovieClickListener? = null



    fun setOnMovieClickListener(listener: SecondOnMovieClickListener) {
        movieClickListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondDynamicHolder {
        return SecondDynamicHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SecondDynamicHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            filmName.text = movie?.nameRu ?: movie?.nameEn ?: movie?.nameOriginal
                //Сделать нормальную проверку рейтинга
            if (movie?.ratingKinopoisk == null) {
                ratingText.text = movie?.ratingImdb.toString()
            } else {
                ratingText.text = movie?.ratingKinopoisk.toString()
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


    interface SecondOnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }
}


class SecondDynamicHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

class SecondDynamicDiffUtilCallBack : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem == newItem

}

