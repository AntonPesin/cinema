package com.example.android_coursework_lvl1.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.viewmodels.HomeViewModel
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.FirstDynamicAdapter
import com.example.android_coursework_lvl1.adapter.PopularAdapter
import com.example.android_coursework_lvl1.adapter.PremieresAdapter
import com.example.android_coursework_lvl1.adapter.SecondDynamicAdapter
import com.example.android_coursework_lvl1.adapter.SeriesAdapter
import com.example.android_coursework_lvl1.adapter.Top250Adapter
import com.example.android_coursework_lvl1.databinding.AllMoviesLayoutBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AllMovies : Fragment() {

    private var _binding: AllMoviesLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = AllMoviesLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.allMoviesBackButton.setOnClickListener {
            findNavController().navigateUp()
        }


        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_allMovies_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_allMovies_to_search)
        binding.navigation.setProfileActionId(R.id.action_allMovies_to_profile)
        binding.navigation.setNavController(navController)


        when (arguments?.getString("category")) {
            getString(R.string.premieres) -> {
                binding.allMoviesTitle.text = getString(R.string.premieres)

                val premieresAdapter = PremieresAdapter()

                binding.allMoviesRv.adapter = premieresAdapter

                viewModel.premieresMovies.onEach {
                    premieresAdapter.setAllData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.popular) -> {
                binding.allMoviesTitle.text = getString(R.string.popular)

                val popularAdapter = PopularAdapter()

                binding.allMoviesRv.adapter = popularAdapter

                viewModel.pagedPopularMovies.onEach {
                    popularAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.top_250) -> {
                binding.allMoviesTitle.text = getString(R.string.top_250)

                val top250Adapter = Top250Adapter()

                binding.allMoviesRv.adapter = top250Adapter

                viewModel.pagedTop250Movies.onEach {
                    top250Adapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.series) -> {
                binding.allMoviesTitle.text = getString(R.string.series)

                val seriesAdapter = SeriesAdapter()

                binding.allMoviesRv.adapter = seriesAdapter

                viewModel.pagedSeries.onEach {
                    seriesAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.usa_actions) -> {
                binding.allMoviesTitle.text = getString(R.string.usa_actions)

                val firstDynamicAdapter = FirstDynamicAdapter()

                binding.allMoviesRv.adapter = firstDynamicAdapter

                viewModel.pagedFirstDynamicMovies.onEach {
                    firstDynamicAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.french_dramas) -> {
                binding.allMoviesTitle.text = getString(R.string.french_dramas)

                val firstDynamicAdapter = FirstDynamicAdapter()

                binding.allMoviesRv.adapter = firstDynamicAdapter

                viewModel.pagedFirstDynamicMovies.onEach {
                    firstDynamicAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.british_comedy) -> {
                binding.allMoviesTitle.text = getString(R.string.british_comedy)

                val firstDynamicAdapter = FirstDynamicAdapter()

                binding.allMoviesRv.adapter = firstDynamicAdapter

                viewModel.pagedFirstDynamicMovies.onEach {
                    firstDynamicAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.japan_anime) -> {
                binding.allMoviesTitle.text = getString(R.string.japan_anime)

                val secondDynamicAdapter = SecondDynamicAdapter()

                binding.allMoviesRv.adapter = secondDynamicAdapter

                viewModel.pagedSecondDynamicMovies.onEach {
                    secondDynamicAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.soviet_military) -> {
                binding.allMoviesTitle.text = getString(R.string.soviet_military)

                val secondDynamicAdapter = SecondDynamicAdapter()

                binding.allMoviesRv.adapter = secondDynamicAdapter

                viewModel.pagedSecondDynamicMovies.onEach {
                    secondDynamicAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }

            getString(R.string.russian_criminal) -> {
                binding.allMoviesTitle.text = getString(R.string.russian_criminal)

                val secondDynamicAdapter = SecondDynamicAdapter()

                binding.allMoviesRv.adapter = secondDynamicAdapter

                viewModel.pagedSecondDynamicMovies.onEach {
                    secondDynamicAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }




    }


}

