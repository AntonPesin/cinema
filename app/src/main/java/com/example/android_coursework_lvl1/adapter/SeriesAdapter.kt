package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.MovieModel

class SeriesAdapter : PagingDataAdapter<MovieModel, SeriesMovieHolder>(SeriesDiffUtilCallback()) {

    private var seriesClickListener: SeriesOnMovieClickListener? = null

    fun setOnSeriesClickListener(listener: SeriesOnMovieClickListener) {
        seriesClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesMovieHolder {
        return SeriesMovieHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SeriesMovieHolder, position: Int) {
        val series = getItem(position)
        holder.binding.filmName.text =
            series?.nameRu ?: series?.nameEn
        holder.binding.genre.text = series?.genres?.joinToString(", ") { it.genre }
        holder.binding.ratingText.text = series?.ratingKinopoisk.toString()
        holder.binding.poster.setOnClickListener {
            seriesClickListener?.onSeriesClick(series)
        }
        series.let {
            Glide
                .with(holder.binding.poster.context)
                .load(it?.posterUrl)
                .override(111, 156)
                .into(holder.binding.poster)
        }
    }

    interface SeriesOnMovieClickListener {
        fun onSeriesClick(series: MovieModel?)
    }
}


class SeriesMovieHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

class SeriesDiffUtilCallback : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean =
        oldItem == newItem

}

