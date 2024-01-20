package com.example.android_coursework_lvl1.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.ImageItemBinding
import com.example.android_coursework_lvl1.data.repository.network.models.ImageModel

class GalleryAdapter : PagingDataAdapter<ImageModel, GalleryViewHolder>(GalleryDiffUtilCallback()) {

    private var imageListener: ImageClickListener? = null

    fun setOnImageClickListener(listener: ImageClickListener) {
        imageListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image = getItem(position)
        with(holder.binding) {
            galleryImage.setOnClickListener {
                imageListener?.onImageClick(image)
            }
            Glide.with(galleryImage.context)
                .load(image?.previewUrl)
                .into(galleryImage)

        }
    }

    interface ImageClickListener {
        fun onImageClick(image: ImageModel?) {
        }
    }
}

class GalleryViewHolder(val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root)

class GalleryDiffUtilCallback : DiffUtil.ItemCallback<ImageModel>() {
    override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
        oldItem.imageUrl == newItem.imageUrl

    override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean =
        oldItem == newItem

}