package com.example.android_coursework_lvl1.pages

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.Navigation
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.ActorsAdapter
import com.example.android_coursework_lvl1.adapter.GalleryAdapter
import com.example.android_coursework_lvl1.adapter.SimilarAdapter
import com.example.android_coursework_lvl1.adapter.StaffAdapter
import com.example.android_coursework_lvl1.databinding.MovieLayoutBinding
import com.example.android_coursework_lvl1.models.StaffModel
import com.example.android_coursework_lvl1.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MoviePage : Fragment() {

    private var _binding: MovieLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: Repository
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var staffAdapter: StaffAdapter
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var similarAdapter: SimilarAdapter
    private lateinit var navigationHandler: Navigation
    private var shouldNavigate = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = MovieLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = Repository(requireContext())
        navigationHandler = Navigation(findNavController())

        val id = arguments?.getInt("id")

        actorsAdapter = ActorsAdapter()
        staffAdapter = StaffAdapter()
        galleryAdapter = GalleryAdapter()
        similarAdapter = SimilarAdapter()

        val actorsRecyclerView = binding.actorsRv
        val staffRecyclerView = binding.staffRv
        val galleryRecyclerView = binding.galleryRv
        val similarRecyclerView = binding.similarRv

        actorsRecyclerView.adapter = actorsAdapter
        staffRecyclerView.adapter = staffAdapter
        galleryRecyclerView.adapter = galleryAdapter
        similarRecyclerView.adapter = similarAdapter


        lifecycleScope.launch {
            id?.let { nonNullId ->
                try {
                    val info = repository.getInfo(nonNullId)
                    val staff = repository.getStaff(nonNullId)
                    val gallery = repository.getStillImage(nonNullId)
                    val similar = repository.getSimilar(nonNullId)

                    actorsAdapter.setData(staff)
                    staffAdapter.setData(staff)
                    galleryAdapter.setData(gallery)
                    similarAdapter.setData(similar)

                    binding.movieName.text = info.nameRu ?: info.nameEn ?: info.nameOriginal

                    binding.actorsAll.text = actorsAdapter.getAllActors().size.toString()
                    binding.staffAll.text = staffAdapter.getAllStaff().size.toString()
                    binding.galleryAll.text = galleryAdapter.getAllImages().size.toString()
                    binding.similarAll.text = similarAdapter.getAllSimilar().size.toString()

                    info.description?.let { binding.description.text = it }
                    info.genres?.let { binding.genre.text = "${it.first().genre}, " }
                    info.countries?.let { binding.country.text = "${it.first().country}, " }
                    info.year?.let { binding.releaseYear.text = "$it, " }
                    binding.ratingAgeLimits.text = when (info.ratingAgeLimits ?: info.ratingMpaa) {
                        "age6" -> "6+"
                        "age12" -> "12+"
                        "pg13" -> "13+"
                        "age16" -> "16+"
                        "R" -> "17+"
                        "NC-17 " -> "18+"
                        "age18" -> "18+"
                        else -> " "
                    }
                    info.filmLength?.let { binding.length.text = "$it мин, " }
                    info.ratingKinopoisk?.let { binding.movieRating.text = "$it, " }
                    Log.d("infoID", "$info")
                    Glide.with(requireContext())
                        .load(info.posterUrl ?: info.posterUrlPreview)
                        .into(binding.poster)

                    binding.actorsAllIcon.setOnClickListener {
                        navigationHandler.navigateMovieToAllActors(nonNullId)
                    }

                    binding.staffAllIcon.setOnClickListener {
                        navigationHandler.navigateMovieToAllStaff(nonNullId)
                    }

                    binding.galleryAllIcon.setOnClickListener {
                        navigationHandler.navigateMovieToGallery(nonNullId)
                    }

                    actorsAdapter.setOnActorsClickListener(object :
                        ActorsAdapter.ActorsClickListener {
                        override fun onActorClick(staff: StaffModel?) {
                            Log.d(" actor ID", "${staff?.staffId}")
                            navigationHandler.navigateMovieToActor(staff?.staffId)
                        }
                    })

                    staffAdapter.setOnStaffClickListener(object : StaffAdapter.StaffClickListener {
                        override fun onStaffClick(staff: StaffModel?) {
                            Log.d(" staff ID", "${staff?.staffId}")
                            navigationHandler.navigateMovieToActor(staff?.staffId)
                        }
                    })

                } catch (e: HttpException) {
                    shouldNavigate = false
                    val errorMessage = getString(R.string.error_description)
                    activity?.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle(getString(R.string.error_title))
                            .setMessage(errorMessage)
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()
                    }
                }
            }
            if (shouldNavigate) {
                val navController = findNavController()
                binding.navigation.setSearchActionId(R.id.action_serialPage_to_search)
                binding.navigation.setProfileActionId(R.id.action_serialPage_to_profile)
                binding.navigation.setHomeActionId(R.id.action_serialPage_to_homepage)
                binding.navigation.setNavController(navController)
            }

        }

        binding.movieBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
