package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.App
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.MovieModel
import java.lang.Integer.min
import java.util.Locale

class PremieresAdapter : RecyclerView.Adapter<PremieresViewHolder>() {

    private var premierMovies: List<MovieModel> = emptyList()
    private var movieClickListener: PremierOnMovieClickListener? = null
    private var showAllItems = false

    fun setOnMovieClickListener(listener: PremierOnMovieClickListener) {
        movieClickListener = listener
    }

    fun setLimitedData(movies: List<MovieModel>) {
        showAllItems = false
        this.premierMovies = movies.take(20)
        notifyDataSetChanged()
    }

    fun setAllData(movies: List<MovieModel>) {
        showAllItems = true
        this.premierMovies = movies
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremieresViewHolder {
        return PremieresViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PremieresViewHolder, position: Int) {
        val movie = premierMovies[position]
        with(holder.binding) {
            filmName.text = movie.nameRu ?: movie.nameEn
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
        return if (showAllItems) {
            premierMovies.size
        } else {
            min(premierMovies.size, 20)
        }
    }

    interface PremierOnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }
}


class PremieresViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root)

