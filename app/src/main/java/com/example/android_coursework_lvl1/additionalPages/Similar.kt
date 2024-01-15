package com.example.android_coursework_lvl1.additionalPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_coursework_lvl1.Navigation
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.PopularAdapter
import com.example.android_coursework_lvl1.adapter.SimilarAdapter
import com.example.android_coursework_lvl1.databinding.SimilarLayoutBinding
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.models.SimilarModel
import com.example.android_coursework_lvl1.repository.Repository
import kotlinx.coroutines.launch

class Similar : Fragment() {

    private var _binding: SimilarLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationHandler: Navigation
    private lateinit var similarAdapter: SimilarAdapter
    private lateinit var repository: Repository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SimilarLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        navigationHandler = Navigation(findNavController())
        repository = Repository(requireContext())
        binding.similarTitle.text = getString(R.string.similar_series)


        val id = arguments?.getInt("id")
        Log.d("id_gallery", "$id")

        similarAdapter = SimilarAdapter()

        lifecycleScope.launch {
            binding.similarRv.adapter = similarAdapter
            similarAdapter.setData(repository.getSimilar(id))
        }

        similarAdapter.setOnMovieClickListener(
            object : SimilarAdapter.SimilarClickListener{
                override fun onMovieClick(movie: SimilarModel?) {
                    Log.d("Movie_ID", "${movie?.filmId}")
                    navigationHandler.navigateToSerialPage(movie?.filmId)
                }
            }
        )

        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_similar_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_similar_to_search)
        binding.navigation.setProfileActionId(R.id.action_similar_to_profile)
        binding.navigation.setNavController(navController)

        binding.similarIconBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}