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
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.BestMoviesAdapter
import com.example.android_coursework_lvl1.additionalPages.ImageDialogFragment
import com.example.android_coursework_lvl1.databinding.PersonpageLayoutBinding
import com.example.android_coursework_lvl1.models.ActorModel
import com.example.android_coursework_lvl1.models.Films
import com.example.android_coursework_lvl1.navigation.Navigation
import com.example.android_coursework_lvl1.viewmodels.ActorViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PersonPage : Fragment() {
    private var _binding: PersonpageLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorViewModel by viewModels()
    private lateinit var bestMoviesAdapter: BestMoviesAdapter
    private lateinit var navigationHandler: Navigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PersonpageLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportFragmentManager = requireActivity().supportFragmentManager
        bestMoviesAdapter = BestMoviesAdapter()

        fun getTopTenFilms(actorModel: ActorModel): List<Films> {
            val films = actorModel.films
            return if (films.isNotEmpty()) {
                films
                    .filter { it.rating != null }
                    .sortedByDescending { it.rating?.toDouble() }
                    .take(10)
            } else {
                emptyList()
            }
        }
        binding.bestRv.adapter = bestMoviesAdapter

        lifecycleScope.launch {
            viewModel.id?.let {
                val data = viewModel.getData()
                binding.name.text = data.nameRu ?: data.nameEn
                binding.professionInfo.text = data.profession
                binding.sexInfo.text = when (data.sex) {
                    "MALE" -> getString(R.string.male)
                    "FEMALE" -> getString(R.string.female)
                    else -> " "
                }

                if (data.age == 0) {
                    binding.ageCategory.visibility = View.GONE
                    binding.ageInfo.visibility = View.GONE
                } else {
                    binding.ageCategory.visibility = View.VISIBLE
                    binding.ageInfo.visibility = View.VISIBLE
                    binding.ageInfo.text = data.age.toString()
                }
                viewModel.bestFilms.onEach {
                    bestMoviesAdapter.setFilms(getTopTenFilms(data))
                }.launchIn(viewLifecycleOwner.lifecycleScope)

                binding.allFilms.text = "${data.films.size} фильмов"

                setCategoryVisibility(binding.ageCategory, data.age == 0)
                setCategoryVisibility(binding.ageInfo, data.age == 0)

                setCategoryVisibility(binding.birthdayCategory, !data.birthday.isNullOrBlank())
                binding.birthdayInfo.text = data.birthday

                setCategoryVisibility(binding.deathCategory, !data.death.isNullOrBlank())
                binding.deathInfo.text = data.death

                Glide.with(requireContext())
                    .load(data.posterUrl)
                    .into(binding.photo)

                binding.photo.setOnClickListener {
                    Log.d("posterUrl", "${data.posterUrl}")
                    val dialogFragment = ImageDialogFragment.newInstance(data.posterUrl)
                    dialogFragment.show(supportFragmentManager, "image_dialog")
                }

            }

        }

        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_personPage_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_personPage_to_search)
        binding.navigation.setProfileActionId(R.id.action_personPage_to_profile)
        binding.navigation.setNavController(navController)
        navigationHandler = Navigation(findNavController())

        binding.personBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.toFilmographyImageview.setOnClickListener {
            navigationHandler.navigateActorToFilmography(viewModel.id)
        }

        bestMoviesAdapter.setOnMovieClickListener(object : BestMoviesAdapter.OnMovieClickListener {
            override fun onMovieClick(movie: Int?) {
                navigationHandler.navigatePersonToMovie(movie)
            }
        })
    }

    private fun setCategoryVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}