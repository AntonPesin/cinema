package com.example.android_coursework_lvl1.additionalPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.SearchLayoutBinding

class Search : Fragment() {
    private var _binding: SearchLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SearchLayoutBinding.inflate(inflater, container, false)


        binding.homeIcon.setOnClickListener {
            findNavController().navigate(R.id.action_search_to_homepage)
        }

        binding.profileIcon.setOnClickListener {
            findNavController().navigate(R.id.action_search_to_profile)
        }

        return binding.root

    }


}