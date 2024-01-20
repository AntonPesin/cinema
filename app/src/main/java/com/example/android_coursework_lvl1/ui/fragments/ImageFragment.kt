package com.example.android_coursework_lvl1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.android_coursework_lvl1.databinding.ImageBinding
import com.example.android_coursework_lvl1.ui.adapters.ImagePagerAdapter

class ImageFragment : DialogFragment() {

    private var _binding: ImageBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val IMAGE_URLS_KEY = "imageUrls"
        const val CURRENT_POSITION_KEY = "currentPosition"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUrlList = arguments?.getStringArrayList(IMAGE_URLS_KEY)
        val currentPosition = arguments?.getInt(CURRENT_POSITION_KEY) ?: 0

        val adapter = ImagePagerAdapter(imageUrlList)
        binding.imageViewPager.adapter = adapter
        binding.imageViewPager.currentItem = currentPosition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}