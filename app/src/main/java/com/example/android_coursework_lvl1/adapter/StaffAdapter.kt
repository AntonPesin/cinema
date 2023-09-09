package com.example.android_coursework_lvl1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.StaffItemBinding
import com.example.android_coursework_lvl1.models.StaffModel

open class StaffAdapter : RecyclerView.Adapter<StaffViewHolder>() {

    private var staffList: List<StaffModel> = emptyList()
    private var staffClickListener: StaffClickListener? = null

    fun setOnStaffClickListener(listener: StaffClickListener) {
        staffClickListener = listener
    }

    fun setData(staff: List<StaffModel>) {
        staffList = staff.filter { it.professionKey != "ACTOR" }
        notifyDataSetChanged()
    }

    fun getAllStaff(): List<StaffModel> {
        return staffList
    }

    fun get20(): List<StaffModel> {
        return staffList.take(20)
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
        val staff = staffList[position]
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

    override fun getItemCount() = staffList.filter { it.professionKey != "ACTOR" }.size

    interface StaffClickListener {
        fun onStaffClick(staff: StaffModel?)
    }
}

class StaffViewHolder(val binding: StaffItemBinding) :
    RecyclerView.ViewHolder(binding.root)