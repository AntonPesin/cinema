package com.example.android_coursework_lvl1.pages

import android.app.AlertDialog
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
import com.example.android_coursework_lvl1.Navigation
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.ActorsAdapter
import com.example.android_coursework_lvl1.adapter.GalleryAdapter
import com.example.android_coursework_lvl1.adapter.SimilarAdapter
import com.example.android_coursework_lvl1.adapter.StaffAdapter
import com.example.android_coursework_lvl1.databinding.SerialLayoutBinding
import com.example.android_coursework_lvl1.models.ImageModel
import com.example.android_coursework_lvl1.models.SimilarModel
import com.example.android_coursework_lvl1.models.StaffModel
import com.example.android_coursework_lvl1.repository.Repository
import com.example.android_coursework_lvl1.viewmodels.SerialViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SerialPage : Fragment() {

    private var _binding: SerialLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: Repository
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var staffAdapter: StaffAdapter
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var similarAdapter: SimilarAdapter
    private lateinit var navigationHandler: Navigation
    private var shouldNavigate = true
    private val serialViewModel: SerialViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SerialLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationHandler = Navigation(findNavController())
        repository = Repository(requireContext())

        actorsAdapter = ActorsAdapter()
        staffAdapter = StaffAdapter()
        galleryAdapter = GalleryAdapter()
        similarAdapter = SimilarAdapter()

        binding.actorsRv.adapter = actorsAdapter
        binding.staffRv.adapter = staffAdapter
        binding.galleryRv.adapter = galleryAdapter
        binding.similarRv.adapter = similarAdapter



        lifecycleScope.launch {
            serialViewModel.idState.collect { newId ->
                updateUIWithNewId(newId)
            }
        }


        val currentId = serialViewModel.idState.value
        if (currentId != null) {
            updateUIWithNewId(currentId)
        }


        galleryAdapter.setOnImageClickListener(object :
            GalleryAdapter.ImageClickListener {
            override fun onImageClick(image: ImageModel?) {
                val imageUrls =
                    galleryAdapter.getAllImages().map { it.imageUrl }
                val currentPosition =
                    galleryAdapter.getAllImages().indexOf(image)
                if (imageUrls.isNotEmpty()) {
                    val fragment = ImagePage()
                    val bundle = Bundle()
                    bundle.putStringArrayList("imageUrls", ArrayList(imageUrls))
                    bundle.putInt("currentPosition", currentPosition)
                    fragment.arguments = bundle
                    fragment.show(
                        parentFragmentManager,
                        ImagePage::class.java.simpleName
                    )
                }
            }
        })

        actorsAdapter.setOnActorsClickListener(object :
            ActorsAdapter.ActorsClickListener {
            override fun onActorClick(staff: StaffModel?) {
                Log.d(" actor ID", "${staff?.staffId}")
                navigationHandler.navigateSerialToActor(staff?.staffId)
            }
        })

        staffAdapter.setOnStaffClickListener(object :
            StaffAdapter.StaffClickListener {
            override fun onStaffClick(staff: StaffModel?) {
                Log.d(" staff ID", "${staff?.staffId}")
                navigationHandler.navigateSerialToActor(staff?.staffId)
            }
        })

        similarAdapter.setOnMovieClickListener(object :
            SimilarAdapter.SimilarClickListener {
            override fun onMovieClick(movie: SimilarModel?) {
                Log.d("movie ID", "${movie?.filmId}")
                serialViewModel.setNewId(movie?.filmId)
            }
        })


        if (shouldNavigate) {
            val navController = findNavController()
            binding.navigation.setSearchActionId(R.id.action_serialPage_to_search)
            binding.navigation.setProfileActionId(R.id.action_serialPage_to_profile)
            binding.navigation.setHomeActionId(R.id.action_serialPage_to_homepage)
            binding.navigation.setNavController(navController)
        }

        binding.serialBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCategoryVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


    private fun updateUIWithNewId(newId: Int) {
        lifecycleScope.launch {
            try {
                val info = serialViewModel.getInfo(newId)
                val seasons = serialViewModel.getSeasons(newId)
                val staff = serialViewModel.getStaff(newId)
                val gallery = serialViewModel.getImages(newId)
                val similar = serialViewModel.getSimilars(newId)

                binding.movieName.text = info.nameRu ?: info.nameEn ?: info.nameOriginal
                info.description?.let { binding.description.text = "$it" }
                info.genres?.let { binding.genre.text = "${it.first().genre}, " }
                info.countries?.let { binding.country.text = "${it.first().country}, " }
                info.year?.let { binding.releaseYear.text = "$it, " }
                binding.ratingAgeLimits.text =
                    when (info.ratingAgeLimits ?: info.ratingMpaa) {
                        "age6" -> "6+"
                        "age12" -> "12+"
                        "pg13" -> "13+"
                        "age16" -> "16+"
                        "R" -> "17+"
                        "NC-17 " -> "18+"
                        "age18" -> "18+"
                        else -> ""
                    }
                info.filmLength?.let { binding.length.text = "$it мин, " }
                info.ratingKinopoisk?.let { binding.movieRating.text = "$it, " }
                Glide.with(requireContext())
                    .load(info.posterUrl ?: info.posterUrlPreview)
                    .into(binding.poster)

                actorsAdapter.setData(staff)
                staffAdapter.setData(staff)
                galleryAdapter.setData(gallery)
                similarAdapter.setData(similar)

                binding.seasons.text = "$seasons сезон"
                binding.seasonsInfo.text = "$seasons сезонов"

                binding.actorsAll.text =
                    actorsAdapter.getAllActors().size.toString()

                binding.staffAll.text = staffAdapter.getAllStaff().size.toString()

                binding.actorsAllIcon.setOnClickListener {
                  //  val currentId = serialViewModel.idState.value
                    navigationHandler.navigateSerialToAllActors(serialViewModel.idState.value)
                }

                binding.staffAllIcon.setOnClickListener {
                    navigationHandler.navigateSerialToAllStaff(serialViewModel.idState.value)
                }

                binding.galleryAll.text =
                    serialViewModel.getTotalImages(newId).toString()

                binding.galleryAllIcon.setOnClickListener {
                    navigationHandler.navigateSerialToGallery(serialViewModel.idState.value)
                }

                binding.similarAll.text =
                    similarAdapter.getAllSimilar().size.toString()

                binding.similarAllIcon.setOnClickListener {
                    navigationHandler.navigateSerialToSimilar(serialViewModel.idState.value)
                }

                setCategoryVisibility(
                    binding.actorsContainer,
                    actorsAdapter.getAllActors().isNotEmpty()
                )
                setCategoryVisibility(
                    binding.staffContainer,
                    staffAdapter.getAllStaff().isNotEmpty()
                )

                setCategoryVisibility(
                    binding.galleryContainer,
                    galleryAdapter.getAllImages().isNotEmpty()
                )
                setCategoryVisibility(
                    binding.similarContainer,
                    similarAdapter.getAllSimilar().isNotEmpty()
                )

            } catch (e: HttpException) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(getString(R.string.error_title))
                    .setMessage(getString(R.string.error_description))
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }


}
