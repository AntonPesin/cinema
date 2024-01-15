package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.Films
class BestMoviesAdapter: RecyclerView.Adapter<BestFilmsViewHolder>() {

    private var bestFilmsList: List<Films> = emptyList()
    private var movieClickListener: OnMovieClickListener? = null

    fun setOnMovieClickListener(listener: OnMovieClickListener) {
        movieClickListener = listener
    }

    fun setFilms(movies: List<Films>) {
        bestFilmsList = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestFilmsViewHolder {
        return BestFilmsViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BestFilmsViewHolder, position: Int) {
        val film = bestFilmsList[position]
        val image = R.drawable.template
        with(holder.binding) {
            poster.setOnClickListener {
                movieClickListener?.onMovieClick(film.filmId)
            }
            filmName.text = film.nameRu ?: film.nameEn
            if (film.rating  == null) {
                holder.binding.frameRating.visibility = View.INVISIBLE
            } else {
                ratingText.text = film.rating
            }
            film.let {
                Glide
                    .with(poster.context)
                    .load(image)
                    .override(111, 156)
                    .into(poster)
            }
        }
    }

    override fun getItemCount() = bestFilmsList.size

    interface OnMovieClickListener {
        fun onMovieClick(movie: Int?)
    }
}

class BestFilmsViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)
