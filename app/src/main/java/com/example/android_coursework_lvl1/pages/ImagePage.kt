package com.example.android_coursework_lvl1.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.adapter.ImagePagerAdapter
import com.example.android_coursework_lvl1.databinding.ImageLayoutBinding

class ImagePage : DialogFragment() {

    private var _binding: ImageLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ImageLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrlList = arguments?.getStringArrayList("imageUrls")
        val currentPosition = arguments?.getInt("currentPosition") ?: 0

        val adapter = ImagePagerAdapter(imageUrlList)
        binding.imageViewPager.adapter = adapter
        binding.imageViewPager.currentItem = currentPosition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}