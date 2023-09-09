package com.example.android_coursework_lvl1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.databinding.ImageItemBinding
import com.example.android_coursework_lvl1.models.ImageModel

class GalleryAdapter : RecyclerView.Adapter<GalleryViewHolder>() {

    private var imageList: List<ImageModel> = emptyList()
    private var imageListener: ImageClickListener? = null

    fun setOnImageClickListener(listener: ImageClickListener) {
        imageListener = listener
    }

    fun setData(image: List<ImageModel>) {
        imageList = image
        notifyDataSetChanged()
    }

    fun getAllImages(): List<ImageModel> {
        return imageList
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
        Log.d("GalleryAdapter", "getItemCount(): ${imageList.size}")
        return imageList.size
    }

    interface ImageClickListener {
        fun onImageClick(image: ImageModel?) {
            Log.d("GalleryAdapter", "Image clicked: ${image?.imageUrl}")
        }
    }
}

class GalleryViewHolder(val binding: ImageItemBinding) :
    RecyclerView.ViewHolder(binding.root)
