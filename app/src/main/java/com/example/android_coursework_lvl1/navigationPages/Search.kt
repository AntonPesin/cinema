package com.example.android_coursework_lvl1.navigationPages

import android.app.AlertDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.SearchAdapter
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.databinding.SearchLayoutBinding
import com.example.android_coursework_lvl1.models.SearchMovieModel
import com.example.android_coursework_lvl1.navigation.Navigation
import com.example.android_coursework_lvl1.viewmodels.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
class Search : Fragment() {
    private var _binding: SearchLayoutBinding? = null
    private lateinit var navigationHandler: Navigation
    private lateinit var searchMovieAdapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SearchLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigation.searchIcon.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
            PorterDuff.Mode.SRC_ATOP
        )



        val repository = Repository(requireContext())

        val type = arguments?.getString("type")
        val country = arguments?.getString("country")
        val genre = arguments?.getString("genre")
        val yearFrom = arguments?.getInt("yearFrom")
        val yearTo = arguments?.getInt("yearTo")
        val ratingFrom = arguments?.getDouble("ratingFrom")
        val ratingTo = arguments?.getDouble("ratingTo")
        val order = arguments?.getString("order")
        var hideWatched = arguments?.getBoolean("watched")

        Log.d("type", "$type")
        Log.d("country", "$country")
        Log.d("genre", "$genre")
        Log.d("yearFrom", "$yearFrom")
        Log.d("yearTo", "$yearTo")
        Log.d("ratingFrom", "$ratingFrom")
        Log.d("ratingTo", "$ratingTo")
        Log.d("order", "$order")
        Log.d("FirstHideWatched", "$hideWatched")


        searchMovieAdapter = SearchAdapter()
        binding.searchRv.adapter = searchMovieAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setKeyword(query)
                lifecycleScope.launch {
                    try {
                        binding.loadingProgressBar.visibility = View.VISIBLE
                        delay(1000)
                        Log.d("SecondHideWatched", "$hideWatched")
                        if (type == null && country == null && genre == null && yearFrom == null && yearTo == null && ratingFrom == null && ratingTo == null && order == null && hideWatched == null) {
                            binding.notFoundTextview.visibility = View.GONE
                            viewModel.pagedSearchMovies.onEach {
                                searchMovieAdapter.submitData(it)
                            }.launchIn(viewLifecycleOwner.lifecycleScope)
                            if (repository.searchKeywordApi(query, 1).isEmpty()) {
                                binding.searchRv.visibility = View.GONE
                                binding.notFoundTextview.visibility = View.VISIBLE
                            } else {
                                binding.searchRv.visibility = View.VISIBLE
                                binding.notFoundTextview.visibility = View.GONE
                            }
                            binding.loadingProgressBar.visibility = View.GONE
                        } else {
                            lifecycleScope.launch {
                                viewModel.getSeenMovies()
                                Log.d("custom_search", "keyword: $query")
                                binding.loadingProgressBar.visibility = View.VISIBLE
                                hideWatched?.let { viewModel.setHideWatched(it) }
                                val customSearchResults = repository.customSearch(
                                    viewModel.getCountryByName(country),
                                    viewModel.getGenreByName(genre),
                                    order,
                                    type,
                                    ratingFrom,
                                    ratingTo,
                                    yearFrom,
                                    yearTo,
                                    query,
                                )
                                viewModel.pagedSearchMovies.onEach {
                                    searchMovieAdapter.submitData(it)
                                }.launchIn(viewLifecycleOwner.lifecycleScope)
                                if (customSearchResults.isEmpty()) {
                                    binding.searchRv.visibility = View.GONE
                                    binding.notFoundTextview.visibility = View.VISIBLE
                                } else {
                                    binding.searchRv.visibility = View.VISIBLE
                                    binding.notFoundTextview.visibility = View.GONE
                                }
                                binding.loadingProgressBar.visibility = View.GONE
                            }
                        }
                    } catch (e: Exception) {
                        binding.loadingProgressBar.visibility = View.GONE
                        handleHttpException()
                    }
                }
                return true

            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })

        searchMovieAdapter.setOnMovieClickListener(object : SearchAdapter.OnMovieClickListener {
            override fun onMovieClick(movie: SearchMovieModel?) {
                navigationHandler.navigateSearchToMovie(movie?.kinopoiskId)
            }
        })



        navigationHandler = Navigation(findNavController())
        binding.navigation.homeIcon.setOnClickListener {
            findNavController().navigate(R.id.action_search_to_homepage)
        }

        binding.navigation.profileIcon.setOnClickListener {
            findNavController().navigate(R.id.action_search_to_profile)
        }

        binding.searchFilter.setOnClickListener {
            findNavController().navigate(R.id.action_search_to_searchSettings)
        }
    }

    private fun handleHttpException() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.title_error))
            .setMessage(getString(R.string.error_description))
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}







