package com.example.android_coursework_lvl1.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.App
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.SeenAdapter
import com.example.android_coursework_lvl1.databinding.CollectionLayoutBinding
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.navigation.Navigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class Collection : Fragment() {

    private var _binding: CollectionLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationHandler: Navigation
    private lateinit var seenAdapter: SeenAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CollectionLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationHandler = Navigation(findNavController())

        val type = arguments?.getString("type")
        val isFirstCustomCollection = arguments?.getBoolean("firstCustom")

        binding.collectionButton.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                binding.collectionTitle.text = type.toString()

                val favoriteDao = App.INSTANCE.favoriteDataBase.favoriteDao()
                val wantSeeDao = App.INSTANCE.wantSeeDataBase.wantSeeDao()
                val seenDao = App.INSTANCE.seenDataBase.seenDao()
                val firstCustomDao = App.INSTANCE.firstCustomDataBase.firstCustomDao()
                val secondCustomDao = App.INSTANCE.secondCustomDataBase.secondCustomDao()
                val movies = when (type) {
                    getString(R.string.favorites) -> favoriteDao.getAll()
                    getString(R.string.want_see) -> wantSeeDao.getAll()
                    getString(R.string.seen) -> seenDao.getAll()
                    else -> {
                        if (isFirstCustomCollection == true) {
                            firstCustomDao.getAll()
                        } else
                            secondCustomDao.getAll()
                    }
                }
                withContext(Dispatchers.Main) {
                    seenAdapter = SeenAdapter(movies)
                    binding.collectionRv.adapter = seenAdapter
                }
                seenAdapter.setOnMovieClickListener(object : SeenAdapter.OnMovieClickListener {
                    override fun onMovieClick(movie: MovieModel?) {
                        navigationHandler.navigateCollectionToMovie(movie?.kinopoiskId)
                    }
                })
            }
            val navController = findNavController()
            binding.navigation.setHomeActionId(R.id.action_collection_to_homepage)
            binding.navigation.setSearchActionId(R.id.action_collection_to_search)
            binding.navigation.setProfileActionId(R.id.action_collection_to_profile)
            binding.navigation.setNavController(navController)


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}