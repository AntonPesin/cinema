package com.example.android_coursework_lvl1.ui.limitedAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.ui.adapters.ActorsViewHolder
import com.example.android_coursework_lvl1.databinding.StaffItemBinding
import com.example.android_coursework_lvl1.data.repository.network.models.StaffModel

class LimitedActorsAdapter : RecyclerView.Adapter<ActorsViewHolder>() {

    companion object {
        const val ACTOR_KEY = "ACTOR"
    }

    private var actorsList: List<StaffModel> = emptyList()
    private var actorsListener: ActorsClickListener? = null
    fun setOnActorsClickListener(listener: ActorsClickListener) {
        actorsListener = listener
    }

    fun setLimitedData(actors: List<StaffModel>) {
        actorsList = actors.filter { it.professionKey == ACTOR_KEY }.take(20)
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

    override fun getItemCount() = actorsList.filter { it.professionKey == ACTOR_KEY }.size

    interface ActorsClickListener {
        fun onActorClick(staff: StaffModel?)
    }
}