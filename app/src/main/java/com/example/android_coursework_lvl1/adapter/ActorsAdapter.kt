package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.StaffItemBinding
import com.example.android_coursework_lvl1.models.StaffModel

class ActorsAdapter : RecyclerView.Adapter<ActorsViewHolder>() {

    private var actorsList: List<StaffModel> = emptyList()
    private var actorsListener: ActorsClickListener? = null

    fun setOnActorsClickListener(listener: ActorsClickListener) {
        actorsListener = listener
    }

    fun setData(actors: List<StaffModel>) {
        actorsList = actors.filter { it.professionKey == "ACTOR" }
        notifyDataSetChanged()
    }

    fun getAllActors(): List<StaffModel> {
        return actorsList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        return ActorsViewHolder(
            StaffItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        val actors = actorsList[position]
        with(holder.binding) {
            staffPoster.setOnClickListener {
                actorsListener?.onActorClick(actors)
            }
            staffName.text = actors.nameRu ?: actors.nameEn ?: " "
            staffDescription.text = actors.description ?: " "
            Glide.with(staffPoster.context)
                .load(actors.posterUrl)
                .override(111, 156)
                .into(staffPoster)
        }
    }

    override fun getItemCount() = actorsList.filter { it.professionKey == "ACTOR" }.size

    interface ActorsClickListener {
        fun onActorClick(staff: StaffModel?)
    }
}

class ActorsViewHolder(val binding: StaffItemBinding) : RecyclerView.ViewHolder(binding.root)
