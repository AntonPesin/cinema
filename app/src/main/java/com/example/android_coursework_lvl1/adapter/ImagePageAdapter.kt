package com.example.android_coursework_lvl1.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide

class ImagePagerAdapter(private val imageUrlList: List<String>?) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE

        imageUrlList?.get(position)?.let { imageUrl ->
            Glide.with(container)
                .load(imageUrl)
                .into(imageView)
        }

        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return imageUrlList?.size ?: 0
    }
}