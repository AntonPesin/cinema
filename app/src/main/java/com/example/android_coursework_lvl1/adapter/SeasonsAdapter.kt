package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_coursework_lvl1.databinding.SeasonItemBinding
import com.example.android_coursework_lvl1.models.SeasonsModel

class SeasonsAdapter : RecyclerView.Adapter<SeasonsViewHolder>() {

    private var seasonsList: List<SeasonsModel> = emptyList()
    private var selectedSeason: Int = 0

    fun setData(seasons: List<SeasonsModel>) {
        this.seasonsList = seasons
        notifyDataSetChanged()
    }

    fun setSelectedSeason(season: Int) {
        this.selectedSeason = season
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonsViewHolder {
        return SeasonsViewHolder(
            SeasonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SeasonsViewHolder, position: Int) {
        val currentData = seasonsList[selectedSeason]
        val episodes = currentData.episodes ?: emptyList()
        if (position < episodes.size) {
            val episode = episodes[position]
            with(holder.binding) {
                if (episode.nameRu == null) {
                    episodeNumber.text = ("${episode.episodeNumber.toString()} серия. ${episode.nameEn}")
                } else {
                    episodeNumber.text = ("${episode.episodeNumber.toString()} серия. ${episode.nameRu}")
                }
                synopsis.text = episode.synopsis ?: " "
                releaseDate.text = episode.releaseDate ?: " "
            }
        }
    }

    override fun getItemCount(): Int {
        val currentData = seasonsList[selectedSeason]
        val episodes = currentData.episodes ?: emptyList()
        return episodes.size
    }
}
class SeasonsViewHolder(val binding: SeasonItemBinding) : RecyclerView.ViewHolder(binding.root)