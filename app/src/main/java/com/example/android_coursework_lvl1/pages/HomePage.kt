package com.example.android_coursework_lvl1.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.Navigation
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.FirstDynamicAdapter
import com.example.android_coursework_lvl1.adapter.PopularAdapter
import com.example.android_coursework_lvl1.adapter.PremieresAdapter
import com.example.android_coursework_lvl1.adapter.SecondDynamicAdapter
import com.example.android_coursework_lvl1.adapter.SeriesAdapter
import com.example.android_coursework_lvl1.adapter.Top250Adapter
import com.example.android_coursework_lvl1.customviews.CustomView
import com.example.android_coursework_lvl1.databinding.HomepageLayoutBinding
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.repository.Repository
import com.example.android_coursework_lvl1.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomePage : Fragment() {

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

        setCustomViews()

    }


    private fun setCustomViews() {
        val premierAdapter = PremieresAdapter()
        val popularAdapter = PopularAdapter()
        val top250Adapter = Top250Adapter()
        val firstDynamicAdapter = FirstDynamicAdapter()
        val secondDynamicAdapter = SecondDynamicAdapter()
        val seriesAdapter = SeriesAdapter()

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
            object : PremieresAdapter.PremierOnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateToMoviePage(movie?.kinopoiskId)
                }
            }
        )
        viewModel.premieresMovies.onEach {
            premierAdapter.setLimitedData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Popular
        popularView?.setRecyclerViewAdapter(popularAdapter)
        popularAdapter.setOnMovieClickListener(
            object : PopularAdapter.PopularOnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.filmId}")
                    navigationHandler.navigateToMoviePage(movie?.filmId)
                }
            }
        )
        viewModel.pagedPopularMovies.onEach {
            popularAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Top250
        top250View?.setRecyclerViewAdapter(top250Adapter)
        top250Adapter.setOnMovieClickListener(
            object : Top250Adapter.Top250OnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.filmId}")
                    navigationHandler.navigateToMoviePage(movie?.filmId)
                }
            }
        )
        viewModel.pagedTop250Movies.onEach {
            top250Adapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //First
        firstDynamicView?.setRecyclerViewAdapter(firstDynamicAdapter)
        firstDynamicAdapter.setOnMovieClickListener(
            object : FirstDynamicAdapter.FirstOnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateToMoviePage(movie?.kinopoiskId)
                }
            }
        )
        viewModel.pagedFirstDynamicMovies.onEach {
            firstDynamicAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Second
        secondDynamicView?.setRecyclerViewAdapter(secondDynamicAdapter)
        secondDynamicAdapter.setOnMovieClickListener(
            object : SecondDynamicAdapter.SecondOnMovieClickListener {
                override fun onMovieClick(movie: MovieModel?) {
                    Log.d("Movie_ID", "${movie?.kinopoiskId}")
                    navigationHandler.navigateToMoviePage(movie?.kinopoiskId)
                }
            }
        )
        viewModel.pagedSecondDynamicMovies.onEach {
            secondDynamicAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        //Series
        seriesView?.setRecyclerViewAdapter(seriesAdapter)
        seriesAdapter.setOnSeriesClickListener(
            object : SeriesAdapter.SeriesOnMovieClickListener {
                override fun onSeriesClick(series: MovieModel?) {
                    Log.d("Series_ID", "${series?.kinopoiskId}")
                    navigationHandler.navigateToSerialPage(series?.kinopoiskId)
                }

            }
        )

        viewModel.pagedSeries.onEach {
            seriesAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

    }


}





