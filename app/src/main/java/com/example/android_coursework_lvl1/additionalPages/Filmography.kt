package com.example.android_coursework_lvl1.additionalPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.FilmogrpahyLayoutBinding

class Filmography : Fragment() {

    private var _binding: FilmogrpahyLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FilmogrpahyLayoutBinding.inflate(inflater, container, false)




        binding.filmographyIconBack.setOnClickListener {
            findNavController().navigateUp()
        }


        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_filmography_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_filmography_to_search)
        binding.navigation.setProfileActionId(R.id.action_filmography_to_profile)
        binding.navigation.setNavController(navController)

        return binding.root

    }
}