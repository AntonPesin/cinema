package com.example.android_coursework_lvl1.additionalPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.ProfileLayoutBinding

class Profile:Fragment() {
    private var _binding: ProfileLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = ProfileLayoutBinding.inflate(inflater, container, false)


        binding.homeIcon.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_homepage)
        }

        binding.searchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_search)
        }

        return binding.root

    }

}