package com.example.android_coursework_lvl1.ui.fragments.collection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.App
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.data.repository.network.models.MovieModel
import com.example.android_coursework_lvl1.databinding.CollectionBinding
import com.example.android_coursework_lvl1.model.enums.CollectionType
import com.example.android_coursework_lvl1.ui.adapters.SeenAdapter
import com.example.android_coursework_lvl1.ui.navigation.Navigation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Collection : Fragment() {

    private var _binding: CollectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationHandler: Navigation
    private lateinit var seenAdapter: SeenAdapter

    companion object {
        const val TYPE_KEY = "type"
        const val NAME_KEY = "name"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationHandler = Navigation(findNavController())

        val type = arguments?.getString(TYPE_KEY)
        val name = arguments?.getString(NAME_KEY)

        binding.collectionButton.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                binding.collectionTitle.text = name

                val favoriteDao = App.INSTANCE.favoriteDataBase.favoriteDao()
                val wantSeeDao = App.INSTANCE.wantSeeDataBase.wantSeeDao()
                val seenDao = App.INSTANCE.seenDataBase.seenDao()
                val firstCustomDao = App.INSTANCE.firstCustomDataBase.firstCustomDao()
                val secondCustomDao = App.INSTANCE.secondCustomDataBase.secondCustomDao()
                Log.d("Collection", type.toString())
                val movies = when (type) {
                    CollectionType.FAVORITE.name -> { favoriteDao.getAll() }
                    CollectionType.WANT_SEE.name -> wantSeeDao.getAll()
                    CollectionType.SEEN.name -> seenDao.getAll()
                    CollectionType.FIRST_CUSTOM.name -> firstCustomDao.getAll()
                    else -> {
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