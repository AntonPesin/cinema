package com.example.android_coursework_lvl1.ui.all

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.model.enums.TypeMovie
import com.example.android_coursework_lvl1.ui.adapters.MovieAdapter
import com.example.android_coursework_lvl1.ui.adapters.PremierAdapter
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel
import com.example.android_coursework_lvl1.databinding.AllMoviesBinding
import com.example.android_coursework_lvl1.ui.navigation.Navigation
import com.example.android_coursework_lvl1.ui.viewmodels.AllMoviesModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AllMoviesFragment : Fragment() {

    private var _binding: AllMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationHandler: Navigation
    private val viewModel: AllMoviesModel by viewModels()

    companion object {
        const val CATEGORY_KEY = "category"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = AllMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val premierAdapter = PremierAdapter()
        val movieAdapter = MovieAdapter()

        navigationHandler = Navigation(findNavController())
        binding.allMoviesBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_allMovies_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_allMovies_to_search)
        binding.navigation.setProfileActionId(R.id.action_allMovies_to_profile)
        binding.navigation.setNavController(navController)

        when (arguments?.get(CATEGORY_KEY)) {
           TypeMovie.PREMIERES -> {
                binding.allMoviesTitle.text = getString(R.string.premieres)
                binding.allMoviesRv.adapter = premierAdapter

                viewModel.premierMovies.onEach {
                    premierAdapter.setAllData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                premierAdapter.setOnMovieClickListener(
                    object : PremierAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }


            TypeMovie.POPULAR -> {
                binding.allMoviesTitle.text = getString(R.string.popular)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedPopularMovies.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }

            TypeMovie.TOP250 -> {
                binding.allMoviesTitle.text = getString(R.string.top_250)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedTop250Movies.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }

            TypeMovie.USA_ACTION -> {
                binding.allMoviesTitle.text = getString(R.string.usa_actions)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedFirstDynamicMovies.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }

            TypeMovie.FRENCH_DRAMAS -> {
                binding.allMoviesTitle.text = getString(R.string.french_dramas)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedFirstDynamicMovies.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }

            TypeMovie.BRITISH_COMEDY -> {
                binding.allMoviesTitle.text = getString(R.string.british_comedy)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedFirstDynamicMovies.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }

            TypeMovie.JAPAN_ANIME -> {
                binding.allMoviesTitle.text = getString(R.string.japan_anime)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedSecondDynamicMovies.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }

            TypeMovie.SOVIET_MILITARY -> {
                binding.allMoviesTitle.text = getString(R.string.soviet_military)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedSecondDynamicMovies.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }

           TypeMovie.RUSSIAN_CRIMINAL -> {
                binding.allMoviesTitle.text = getString(R.string.russian_criminal)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedSecondDynamicMovies.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Movie_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }
                    }
                )
            }

           TypeMovie.SERIES -> {
                binding.allMoviesTitle.text = getString(R.string.series)
                binding.allMoviesRv.adapter = movieAdapter

                viewModel.pagedSeries.onEach {
                    movieAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                movieAdapter.setOnMovieClickListener(
                    object : MovieAdapter.OnMovieClickListener {
                        override fun onMovieClick(movie: MovieModel?) {
                            Log.d("Series_ID", "${movie?.kinopoiskId}")
                            navigationHandler.navigateAllMoviesToMoviePage(movie?.kinopoiskId)
                        }

                    }
                )
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

