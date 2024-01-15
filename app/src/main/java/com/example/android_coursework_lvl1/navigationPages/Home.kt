package com.example.android_coursework_lvl1.navigationPages

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.PremierAdapter
import com.example.android_coursework_lvl1.customviews.CustomView
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.databinding.HomepageLayoutBinding
import com.example.android_coursework_lvl1.limitedAdapter.LimitedFirstDynamicAdapter
import com.example.android_coursework_lvl1.limitedAdapter.LimitedPopularAdapter
import com.example.android_coursework_lvl1.limitedAdapter.LimitedSecondDynamicAdapter
import com.example.android_coursework_lvl1.limitedAdapter.LimitedSeriesAdapter
import com.example.android_coursework_lvl1.limitedAdapter.LimitedTop250Adapter
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.navigation.Navigation
import com.example.android_coursework_lvl1.viewmodels.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
class Home : Fragment() {
    private var _binding: HomepageLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var repository: Repository
    private lateinit var navigationHandler: Navigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = HomepageLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = Repository(requireContext())

        navigationHandler = Navigation(findNavController())
        val navController = findNavController()
        binding.navigation.setSearchActionId(R.id.action_homepage_to_search)
        binding.navigation.setProfileActionId(R.id.action_homepage_to_profile)
        binding.navigation.setNavController(navController)

        binding.homeViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.navigation.homeIcon.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
            PorterDuff.Mode.SRC_ATOP
        )

        setCustomViews()

    }


    private fun setCustomViews() {
        val premierAdapter = PremierAdapter()
        val popularAdapter = LimitedPopularAdapter()
        val top250Adapter = LimitedTop250Adapter()
        val firstDynamicAdapter = LimitedFirstDynamicAdapter()
        val secondDynamicAdapter = LimitedSecondDynamicAdapter()
        val seriesAdapter = LimitedSeriesAdapter()

        val premierView = view?.findViewById<CustomView>(R.id.custom_view1)
        val popularView = view?.findViewById<CustomView>(R.id.custom_view2)
        val top250View = view?.findViewById<CustomView>(R.id.custom_view3)
        val firstDynamicView = view?.findViewById<CustomView>(R.id.custom_view4)
        val secondDynamicView = view?.findViewById<CustomView>(R.id.custom_view5)
        val seriesView = view?.findViewById<CustomView>(R.id.custom_view6)

        premierView?.setTitleText(getString(R.string.premieres))
        popularView?.setTitleText(getString(R.string.popular))
        top250View?.setTitleText(getString(R.string.top_250))
        firstDynamicView?.setTitleText(repository.categoryF)
        secondDynamicView?.setTitleText(repository.categoryS)
        seriesView?.setTitleText(getString(R.string.series))

        premierView?.setRecyclerViewAdapter(premierAdapter)
        popularView?.setRecyclerViewAdapter(popularAdapter)
        top250View?.setRecyclerViewAdapter(top250Adapter)
        firstDynamicView?.setRecyclerViewAdapter(firstDynamicAdapter)
        secondDynamicView?.setRecyclerViewAdapter(secondDynamicAdapter)
        seriesView?.setRecyclerViewAdapter(seriesAdapter)

        premierView?.getAllMovies(getString(R.string.premieres))
        popularView?.getAllMovies(getString(R.string.popular))
        top250View?.getAllMovies(getString(R.string.top_250))
        firstDynamicView?.getAllMovies(repository.categoryF)
        secondDynamicView?.getAllMovies(repository.categoryS)
        seriesView?.getAllMovies(getString(R.string.series))


        //Premier
        premierView?.setRecyclerViewAdapter(premierAdapter)
        premierAdapter.setOnMovieClickListener(
            object : PremierAdapter.OnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateHomePageToMoviePage(movie?.kinopoiskId)
                }
            }
        )
        viewModel.premierMovies.onEach {
            premierAdapter.setLimitedData(it)
            premierView?.check(premierAdapter)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Popular
        popularView?.setRecyclerViewAdapter(popularAdapter)
        popularAdapter.setOnMovieClickListener(
            object : LimitedPopularAdapter.OnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateHomePageToMoviePage(movie?.kinopoiskId)
                }
            }
        )
        viewModel.popularLimitedMovies.onEach {
            popularAdapter.setLimitedData(it)
            popularView?.check(popularAdapter)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Top250
        top250View?.setRecyclerViewAdapter(top250Adapter)
        top250Adapter.setOnMovieClickListener(
            object : LimitedTop250Adapter.OnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateHomePageToMoviePage(movie?.kinopoiskId)
                }
            }
        )
        viewModel.top250LimitedMovies.onEach {
            top250Adapter.setLimitedData(it)
            top250View?.check(top250Adapter)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //First
        firstDynamicView?.setRecyclerViewAdapter(firstDynamicAdapter)
        firstDynamicAdapter.setOnMovieClickListener(
            object : LimitedFirstDynamicAdapter.OnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateHomePageToMoviePage(movie?.kinopoiskId)
                }
            }
        )
        viewModel.firstDynamicLimitedMovies.onEach {
            firstDynamicAdapter.setLimitedData(it)
            firstDynamicView?.check(firstDynamicAdapter)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Second
        secondDynamicView?.setRecyclerViewAdapter(secondDynamicAdapter)
        secondDynamicAdapter.setOnMovieClickListener(
            object : LimitedSecondDynamicAdapter.OnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateHomePageToMoviePage(movie?.kinopoiskId)
                }
            }
        )
        viewModel.secondDynamicLimitedMovies.onEach {
            secondDynamicAdapter.setLimitedData(it)
            secondDynamicView?.check(secondDynamicAdapter)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Series
        seriesView?.setRecyclerViewAdapter(seriesAdapter)
        seriesAdapter.setOnSeriesClickListener(
            object : LimitedSeriesAdapter.SeriesTestOnMovieClickListener {
                override fun onSeriesTestClick(movie: MovieModel?) {
                    Log.d("Series_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateHomePageToMoviePage(movie?.kinopoiskId)
                }

            }
        )

        viewModel.seriesLimitedMovies.onEach {
            seriesAdapter.setLimitedData(it)
            seriesView?.check(seriesAdapter)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        fun checkData() {
            if (premierAdapter.itemCount == 0 && popularAdapter.itemCount == 0 && top250Adapter.itemCount == 0 && firstDynamicAdapter.itemCount == 0 && secondDynamicAdapter.itemCount == 0 && seriesAdapter.itemCount == 0) {
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(
                    requireContext(),
                    getString(R.string.error_description),
                    duration
                )
                toast.show()
            }
        }

        lifecycleScope.launch {
            delay(2000)
            checkData()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}






