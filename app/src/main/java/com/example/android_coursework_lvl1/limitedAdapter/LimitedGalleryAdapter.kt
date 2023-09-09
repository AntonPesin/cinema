package com.example.android_coursework_lvl1.limitedAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.adapter.GalleryAdapter
import com.example.android_coursework_lvl1.adapter.GalleryViewHolder
import com.example.android_coursework_lvl1.databinding.ImageItemBinding
import com.example.android_coursework_lvl1.databinding.MovieItemBinding
import com.example.android_coursework_lvl1.models.ImageModel
import com.example.android_coursework_lvl1.models.MovieModel

class LimitedGalleryAdapter : RecyclerView.Adapter<LimitedGalleryViewHolder>() {

    private var imageList: List<ImageModel> = emptyList()
    private var imageListener: ImageClickListener? = null

    fun setOnImageClickListener(listener: ImageClickListener) {
        imageListener = listener
    }

    fun setData(image: List<ImageModel>) {
        imageList = image.take(20)
        notifyDataSetChanged()
    }

    fun getAllImages(): List<ImageModel> {
        return imageList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LimitedGalleryViewHolder {
        return LimitedGalleryViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LimitedGalleryViewHolder, position: Int) {
        val image = imageList[position]
        with(holder.binding) {
            galleryImage.setOnClickListener {
                imageListener?.onImageClick(image)
            }
            Glide.with(galleryImage.context)
                .load(image.previewUrl)
                .into(galleryImage)

        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    interface ImageClickListener {
        fun onImageClick(image: ImageModel?) {
        }
    }

}

class LimitedGalleryViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root)