package com.example.android_coursework_lvl1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.model.enums.Keys
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.ui.adapters.SeasonsAdapter
import com.example.android_coursework_lvl1.data.repository.Repository
import com.example.android_coursework_lvl1.databinding.SeasonsBinding
import com.example.android_coursework_lvl1.ui.navigation.Navigation
import kotlinx.coroutines.launch

class SeasonsFragment : Fragment() {

    private var _binding: SeasonsBinding? = null
    private val binding get() = _binding!!
    private lateinit var seasonsAdapter: SeasonsAdapter
    private lateinit var navigationHandler: Navigation
    private var selectedSeason: Int = 1



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(Keys.ID.name)
        val serialName = arguments?.getString(Keys.NAME.name)
        val repository = Repository(requireContext())

        navigationHandler = Navigation(findNavController())

        val navController = findNavController()
        binding.navigation.setSearchActionId(R.id.action_seasonsPage_to_search)
        binding.navigation.setProfileActionId(R.id.action_seasonsPage_to_profile)
        binding.navigation.setHomeActionId(R.id.action_seasonsPage_to_homepage)
        binding.navigation.setNavController(navController)

        binding.seasonsIconBack.setOnClickListener {
            findNavController().navigateUp()
        }

        seasonsAdapter = SeasonsAdapter()
        lifecycleScope.launch {
            val serialID = repository.getSeasons(id)
            val total = repository.totalSeasons(id)
            val spinner = binding.spinner
            spinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                (1..total).toList()
            )
            binding.serialName.text = serialName
            binding.seasonNumber.text = "$total сезонов"
            seasonsAdapter.setData(serialID)
            binding.seasonsRv.adapter = seasonsAdapter

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedSeason = position
                    seasonsAdapter.setSelectedSeason(selectedSeason)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}