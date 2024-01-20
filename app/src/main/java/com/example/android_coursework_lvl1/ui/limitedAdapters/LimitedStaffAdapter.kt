package com.example.android_coursework_lvl1.ui.limitedAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.ui.adapters.StaffViewHolder
import com.example.android_coursework_lvl1.databinding.StaffItemBinding
import com.example.android_coursework_lvl1.data.repository.network.models.StaffModel

open class LimitedStaffAdapter : RecyclerView.Adapter<StaffViewHolder>() {

    private var limitedStaffList: List<StaffModel> = emptyList()
    private var staffClickListener: StaffClickListener? = null

    companion object {
        const val ACTOR_KEY = "ACTOR"
    }

    fun setOnStaffClickListener(listener: StaffClickListener) {
        staffClickListener = listener
    }
    fun setLimitedData(staff: List<StaffModel>) {
        limitedStaffList = staff.filter { it.professionKey != ACTOR_KEY }.take(20)
        notifyDataSetChanged()
    }

    fun getAllStaff(): List<StaffModel> {
        return limitedStaffList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        return StaffViewHolder(
            StaffItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staff = limitedStaffList[position]
        with(holder.binding) {
            staffPoster.setOnClickListener {
                staffClickListener?.onStaffClick(staff)
            }
            staffName.text = staff.nameRu ?: staff.nameEn ?: " "
            staffDescription.text = staff.description ?: " "
            Glide.with(staffPoster.context)
                .load(staff.posterUrl)
                .override(111, 156)
                .into(staffPoster)
        }
    }

    override fun getItemCount() = limitedStaffList.filter { it.professionKey != ACTOR_KEY }.size

    interface StaffClickListener {
        fun onStaffClick(staff: StaffModel?)
    }
}