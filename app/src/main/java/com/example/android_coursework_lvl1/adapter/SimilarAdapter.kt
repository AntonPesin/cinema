package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.SimilarItemBinding
import com.example.android_coursework_lvl1.models.MovieModel

class SimilarAdapter : RecyclerView.Adapter<SimilarViewHolder>() {

    private var movieList: List<MovieModel> = emptyList()
    private var limitedMovieList: List<MovieModel> = emptyList()
    private var onMovieClickListener: OnMovieClickListener? = null

    fun setOnMovieClickListener(listener: OnMovieClickListener) {
        onMovieClickListener = listener
    }

    fun setData(movie: List<MovieModel>) {
        movieList = movie
        limitedMovieList = movieList
        notifyDataSetChanged()
    }

    fun getAllSimilar(): List<MovieModel> {
        return movieList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        return SimilarViewHolder(
            SimilarItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        val movie = movieList[position]
        with(holder.binding) {
            poster.setOnClickListener {
                onMovieClickListener?.onMovieClick(movie)
            }
            filmName.text = movie.nameRu ?: movie.nameEn ?: movie.nameOriginal ?: ""
            movie.let {
                Glide
                    .with(poster.context)
                    .load(it.posterUrl)
                    .override(111, 156)
                    .into(poster)
            }
        }
    }

    override fun getItemCount() = limitedMovieList.size

    interface OnMovieClickListener {
        fun onMovieClick(movie: MovieModel?)
    }
}
class SimilarViewHolder(val binding: SimilarItemBinding) :
    RecyclerView.ViewHolder(binding.root)