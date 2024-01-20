package com.example.android_coursework_lvl1.ui.limitedAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel

class LimitedSeriesAdapter : RecyclerView.Adapter<LimitedSeriesViewHolder>() {

    private var series: List<MovieModel> = emptyList()
    private var seriesClickListener: SeriesTestOnMovieClickListener? = null

    fun setOnSeriesClickListener(listener: SeriesTestOnMovieClickListener) {
        seriesClickListener = listener
    }

    fun setLimitedData(movies: List<MovieModel>) {
        this.series = movies.take(20)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LimitedSeriesViewHolder {
        return LimitedSeriesViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LimitedSeriesViewHolder, position: Int) {
        val series = series[position]
        with(holder.binding) {
            filmName.text = series.nameRu ?: series.nameEn ?: series.nameOriginal
            val rating = series.ratingKinopoisk ?: series.ratingImdb
            if (rating == null) {
                frameRating.visibility = View.INVISIBLE
            } else {
                ratingText.text = rating
            }
            genre.text = series.genres?.joinToString(", ") { it.genre }
            poster.setOnClickListener {
                seriesClickListener?.onSeriesTestClick(series)
            }
            series.let {
                Glide
                    .with(poster.context)
                    .load(it.posterUrl)
                    .override(111, 156)
                    .into(poster)
            }
        }
    }

    override fun getItemCount(): Int {
        return series.size
    }

    interface SeriesTestOnMovieClickListener {
        fun onSeriesTestClick(movie: MovieModel?)
    }
}

class LimitedSeriesViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)