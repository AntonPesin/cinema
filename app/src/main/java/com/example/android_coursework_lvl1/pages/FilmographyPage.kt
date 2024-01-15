package com.example.android_coursework_lvl1.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.BestMoviesAdapter
import com.example.android_coursework_lvl1.databinding.FilmogrpahyLayoutBinding
import com.example.android_coursework_lvl1.navigation.Navigation
import com.example.android_coursework_lvl1.viewmodels.FilmographyViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FilmographyPage : Fragment() {

    private var _binding: FilmogrpahyLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilmographyViewModel by viewModels()
    private lateinit var bestMoviesAdapter: BestMoviesAdapter
    private lateinit var navigationHandler: Navigation
    private var currentCategory: String = "actor"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FilmogrpahyLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bestMoviesAdapter = BestMoviesAdapter()

        val id = arguments?.getInt("id")

        //Почему-то у некоторых людей может быть два professionKeys herself and himself
        binding.filmographyRv.adapter = bestMoviesAdapter

        lifecycleScope.launch {
            viewModel.id?.let {
                val data = viewModel.getData(id)
                binding.name.text = data.nameRu ?: data.nameEn
                var selfCount = 0
                var titrCount = 0

                if (data.sex == "FEMALE") {
                    selfCount =
                        data.films.filter { it.professionKey == "HERSELF" }.size + data.films.filter { it.professionKey == "HIMSELF" }.size
                    titrCount = data.films.filter { it.professionKey == "HRONO_TITR_FEMALE" }.size
                } else if (data.sex == "MALE") {
                    selfCount =
                        data.films.filter { it.professionKey == "HIMSELF" }.size + data.films.filter { it.professionKey == "HERSELF" }.size
                    titrCount = data.films.filter { it.professionKey == "HRONO_TITR_MALE" }.size
                }

                val actorCount = data.films.filter { it.professionKey == "ACTOR" }.size
                val directorCount = data.films.filter { it.professionKey == "DIRECTOR" }.size
                val producerCount = data.films.filter { it.professionKey == "PRODUCER" }.size
                val writerCount = data.films.filter { it.professionKey == "WRITER" }.size

                setCategoryVisibility(binding.actor, actorCount > 0)
                setCategoryVisibility(binding.director, directorCount > 0)
                setCategoryVisibility(binding.producer, producerCount > 0)
                setCategoryVisibility(binding.self, selfCount > 0)
                setCategoryVisibility(binding.writer, writerCount > 0)
                setCategoryVisibility(binding.hronoTitr, titrCount > 0)

                if (data.sex == "FEMALE") {
                    binding.actor.text = getString(R.string.actor_female) + " " + actorCount
                    binding.self.text = getString(R.string.herself) + " " + selfCount
                } else if (data.sex == "MALE") {
                    binding.actor.text = getString(R.string.actor_male) + " " + actorCount
                    binding.self.text = getString(R.string.himself) + " " + selfCount
                }

                binding.director.text = getString(R.string.director) + " " + directorCount
                binding.producer.text = getString(R.string.producer) + " " + producerCount
                binding.writer.text = getString(R.string.writer) + " " + writerCount
                binding.hronoTitr.text = getString(R.string.hrono_titr) + " " + titrCount


                if (actorCount > 0) {
                    viewModel.bestFilms.onEach {
                        currentCategory = "actor"
                        bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "ACTOR" })
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                } else if (directorCount > 0) {
                    viewModel.bestFilms.onEach {
                        currentCategory = "director"
                        bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "DIRECTOR" })
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                } else if (producerCount > 0) {
                    viewModel.bestFilms.onEach {
                        currentCategory = "producer"
                        bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "PRODUCER" })
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                } else if (selfCount > 0) {
                    viewModel.bestFilms.onEach {
                        currentCategory = "self"
                        bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "HERSELF" } + data.films.filter { it.professionKey == "HIMSELF" })
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                } else if (writerCount > 0) {
                    viewModel.bestFilms.onEach {
                        currentCategory = "writer"
                        bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "WRITER" })
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                } else if (titrCount > 0) {
                    viewModel.bestFilms.onEach {
                        currentCategory = "hronoTitr"
                        bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "HRONO_TITR_FEMALE" } + data.films.filter { it.professionKey == "HRONO_TITR_MALE" })
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
                }

                binding.actor.setOnClickListener {
                    if (currentCategory != "actor") {
                        currentCategory = "actor"

                        binding.director.isEnabled = true
                        binding.producer.isEnabled = true
                        binding.writer.isEnabled = true
                        binding.self.isEnabled = true
                        binding.hronoTitr.isEnabled = true
                        binding.actor.isEnabled = false

                        updateCategorySelection()

                        viewModel.bestFilms.onEach {
                            bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "ACTOR" })
                        }.launchIn(viewLifecycleOwner.lifecycleScope)
                    }
                }

                binding.director.setOnClickListener {
                    if (currentCategory != "director") {
                        currentCategory = "director"

                        binding.producer.isEnabled = true
                        binding.writer.isEnabled = true
                        binding.self.isEnabled = true
                        binding.hronoTitr.isEnabled = true
                        binding.actor.isEnabled = true
                        binding.director.isEnabled = false

                        updateCategorySelection()

                        viewModel.bestFilms.onEach {
                            bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "DIRECTOR" })
                        }.launchIn(viewLifecycleOwner.lifecycleScope)
                    }
                }

                binding.producer.setOnClickListener {
                    if (currentCategory != "producer") {
                        currentCategory = "producer"

                        binding.producer.isEnabled = false
                        binding.writer.isEnabled = true
                        binding.self.isEnabled = true
                        binding.hronoTitr.isEnabled = true
                        binding.actor.isEnabled = true
                        binding.director.isEnabled = true

                        updateCategorySelection()

                        viewModel.bestFilms.onEach {
                            bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "PRODUCER" })
                        }.launchIn(viewLifecycleOwner.lifecycleScope)
                    }
                }

                binding.self.setOnClickListener {
                    if (currentCategory != "self") {
                        currentCategory = "self"

                        binding.producer.isEnabled = true
                        binding.writer.isEnabled = true
                        binding.self.isEnabled = false
                        binding.hronoTitr.isEnabled = true
                        binding.actor.isEnabled = true
                        binding.director.isEnabled = true

                        updateCategorySelection()

                        viewModel.bestFilms.onEach {
                            bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "HERSELF" } + data.films.filter { it.professionKey == "HIMSELF" })
                        }.launchIn(viewLifecycleOwner.lifecycleScope)
                    }
                }

                binding.writer.setOnClickListener {
                    if (currentCategory != "writer") {
                        currentCategory = "writer"

                        binding.producer.isEnabled = true
                        binding.writer.isEnabled = false
                        binding.self.isEnabled = true
                        binding.hronoTitr.isEnabled = true
                        binding.actor.isEnabled = true
                        binding.director.isEnabled = true

                        updateCategorySelection()

                        viewModel.bestFilms.onEach {
                            bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "WRITER" })
                        }.launchIn(viewLifecycleOwner.lifecycleScope)
                    }
                }

                binding.hronoTitr.setOnClickListener {
                    if (currentCategory != "hronoTitr") {
                        currentCategory = "hronoTitr"

                        binding.producer.isEnabled = true
                        binding.writer.isEnabled = true
                        binding.self.isEnabled = true
                        binding.hronoTitr.isEnabled = false
                        binding.actor.isEnabled = true
                        binding.director.isEnabled = true

                        updateCategorySelection()

                        viewModel.bestFilms.onEach {
                            bestMoviesAdapter.setFilms(data.films.filter { it.professionKey == "HRONO_TITR_FEMALE" } + data.films.filter { it.professionKey == "HRONO_TITR_MALE" })
                        }.launchIn(viewLifecycleOwner.lifecycleScope)
                    }
                }
            }
        }

        bestMoviesAdapter.setOnMovieClickListener(object : BestMoviesAdapter.OnMovieClickListener {
            override fun onMovieClick(movie: Int?) {
                navigationHandler.navigateFilmographyToMovie(movie)
            }
        })

        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_filmography_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_filmography_to_search)
        binding.navigation.setProfileActionId(R.id.action_filmography_to_profile)
        binding.navigation.setNavController(navController)
        navigationHandler = Navigation(findNavController())

        binding.filmographyIconBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setCategoryVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun updateCategorySelection() {
        binding.actor.setChipBackgroundColorResource(if (currentCategory == "actor") R.color.skillbox_blue else R.color.chip_background)
        binding.actor.setTextColor(
            if (currentCategory == "actor") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.director.setChipBackgroundColorResource(if (currentCategory == "director") R.color.skillbox_blue else R.color.chip_background)
        binding.director.setTextColor(
            if (currentCategory == "director") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.producer.setChipBackgroundColorResource(if (currentCategory == "producer") R.color.skillbox_blue else R.color.chip_background)
        binding.producer.setTextColor(
            if (currentCategory == "producer") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.self.setChipBackgroundColorResource(if (currentCategory == "self") R.color.skillbox_blue else R.color.chip_background)
        binding.self.setTextColor(
            if (currentCategory == "self") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.writer.setChipBackgroundColorResource(if (currentCategory == "writer") R.color.skillbox_blue else R.color.chip_background)
        binding.writer.setTextColor(
            if (currentCategory == "writer") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.hronoTitr.setChipBackgroundColorResource(if (currentCategory == "hronoTitr") R.color.skillbox_blue else R.color.chip_background)
        binding.hronoTitr.setTextColor(
            if (currentCategory == "hronoTitr") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}