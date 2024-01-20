package com.example.android_coursework_lvl1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.model.enums.Keys
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.ui.adapters.SimilarAdapter
import com.example.android_coursework_lvl1.data.repository.Repository
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel
import com.example.android_coursework_lvl1.databinding.SimilarBinding
import com.example.android_coursework_lvl1.ui.navigation.Navigation
import kotlinx.coroutines.launch

class SimilarFragment : Fragment() {

    private var _binding: SimilarBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationHandler: Navigation
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SimilarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationHandler = Navigation(findNavController())
        repository = Repository(requireContext())

        val id = arguments?.getInt(Keys.ID.name)

        binding.similarTitle.text = getString(R.string.similar_movies)

        val similarAdapter = SimilarAdapter()
        binding.similarRv.adapter = similarAdapter

        lifecycleScope.launch {
            similarAdapter.setData(repository.getSimilar(id))
        }

        similarAdapter.setOnMovieClickListener(object : SimilarAdapter.OnMovieClickListener {
            override fun onMovieClick(movie: MovieModel?) {
                navigationHandler.navigateSimilarToMovie(movie?.filmId)
            }
        })

        val navController = findNavController()
        with(binding.navigation) {
            setHomeActionId(R.id.action_similar_to_homepage)
            setSearchActionId(R.id.action_similar_to_search)
            setProfileActionId(R.id.action_similar_to_profile)
            setNavController(navController)
        }

        binding.similarIconBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}