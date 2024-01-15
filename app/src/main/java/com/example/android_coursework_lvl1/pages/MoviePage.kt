package com.example.android_coursework_lvl1.pages

import android.app.AlertDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.ActorsAdapter
import com.example.android_coursework_lvl1.adapter.SimilarAdapter
import com.example.android_coursework_lvl1.adapter.StaffAdapter
import com.example.android_coursework_lvl1.collection.CollectionDialog
import com.example.android_coursework_lvl1.databinding.MovieLayoutBinding
import com.example.android_coursework_lvl1.limitedAdapter.LimitedActorsAdapter
import com.example.android_coursework_lvl1.limitedAdapter.LimitedGalleryAdapter
import com.example.android_coursework_lvl1.limitedAdapter.LimitedStaffAdapter
import com.example.android_coursework_lvl1.models.ImageModel
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.models.StaffModel
import com.example.android_coursework_lvl1.navigation.Navigation
import com.example.android_coursework_lvl1.viewmodels.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

private const val MAX_ELEMENTS = 250

class MoviePage : Fragment() {
    private var _binding: MovieLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var limitedActorsAdapter: LimitedActorsAdapter
    private lateinit var staffAdapter: StaffAdapter
    private lateinit var limitedStaffAdapter: LimitedStaffAdapter
    private lateinit var galleryAdapter: LimitedGalleryAdapter
    private lateinit var similarAdapter: SimilarAdapter
    private lateinit var navigationHandler: Navigation
    private val viewModel: MovieViewModel by viewModels()
    private val type = "movie"
    private var isCollapsed = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MovieLayoutBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            delay(2000)
            viewModel.isLoading.collect { isLoading ->
                binding.navigation.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.mainFrame.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.movieProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.progressOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        navigationHandler = Navigation(findNavController())
        actorsAdapter = ActorsAdapter()
        limitedActorsAdapter = LimitedActorsAdapter()
        staffAdapter = StaffAdapter()
        limitedStaffAdapter = LimitedStaffAdapter()
        galleryAdapter = LimitedGalleryAdapter()
        similarAdapter = SimilarAdapter()

        binding.actorsRv.adapter = limitedActorsAdapter
        binding.staffRv.adapter = limitedStaffAdapter
        binding.galleryRv.adapter = galleryAdapter
        binding.similarRv.adapter = similarAdapter

        galleryAdapter.setOnImageClickListener(object :
            LimitedGalleryAdapter.ImageClickListener {
            override fun onImageClick(image: ImageModel?) {
                val imageUrls = galleryAdapter.getAllImages().map { it.imageUrl }
                val currentPosition = galleryAdapter.getAllImages().indexOf(image)
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

        limitedActorsAdapter.setOnActorsClickListener(object :
            LimitedActorsAdapter.ActorsClickListener {
            override fun onActorClick(staff: StaffModel?) {
                Log.d(" actor ID", "${staff?.staffId}")
                navigationHandler.navigateMovieToActor(staff?.staffId)
            }
        })

        limitedStaffAdapter.setOnStaffClickListener(object :
            LimitedStaffAdapter.StaffClickListener {
            override fun onStaffClick(staff: StaffModel?) {
                Log.d(" staff ID", "${staff?.staffId}")
                navigationHandler.navigateMovieToActor(staff?.staffId)
            }
        })

        similarAdapter.setOnMovieClickListener(object :
            SimilarAdapter.OnMovieClickListener {
            override fun onMovieClick(movie: MovieModel?) {
                Log.d("movie ID", "${movie?.filmId}")
                viewModel.setNewId(movie?.filmId)
            }
        })

        val navController = findNavController()
        binding.navigation.setSearchActionId(R.id.action_moviePage_to_search)
        binding.navigation.setProfileActionId(R.id.action_moviePage_to_profile)
        binding.navigation.setHomeActionId(R.id.action_moviePage_to_homepage)
        binding.navigation.setNavController(navController)

        binding.movieBackButton.setOnClickListener {
            findNavController().navigateUp()
        }


        lifecycleScope.launch {
            start()
        }


    }


    private suspend fun getMovie(id: Int?) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val movie = viewModel.getInfo(id)
                    Log.d("movie", "${viewModel.getInfo(id)}")
                    viewModel.addInteresting()
                    try {
                        withContext(Dispatchers.Main) {
                            movieData(id, movie)
                            getApiData(id, movie)
                        }
                    } catch (e: HttpException) {
                        withContext(Dispatchers.Main) {
                            movieData(id, movie)
                        }
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    handleHttpException()
                }
            }
        }
    }

    private suspend fun start() {
        viewModel.idState.collect { newId ->
            lifecycleScope.launch {
                getMovie(newId)
                checkStatus()
            }
        }
    }


    private suspend fun movieData(id: Int?, movie: MovieModel) {
        val movieName = movie.nameRu ?: movie.nameEn ?: movie.nameOriginal
        binding.genre.text =
            movie.genres?.firstOrNull()?.genre.takeIf { it != null }?.let { "$it, " } ?: ""
        binding.country.text =
            movie.countries?.firstOrNull()?.country.takeIf { it != null }?.let { "$it, " } ?: ""
        binding.releaseYear.text = movie.year?.toString()?.let { "$it, " } ?: ""
        binding.length.text = movie.filmLength?.let { "$it мин, " } ?: ""
        binding.movieRating.text = movie.ratingKinopoisk?.let { "$it, " } ?: ""
        Glide.with(requireContext())
            .load(movie.posterUrl ?: movie.posterUrlPreview)
            .into(binding.poster)
        binding.movieName.text = movieName.toString()
        binding.description.text = movie.description ?: " "
        if (binding.description.text.length > MAX_ELEMENTS) {
            binding.description.maxLines = 5
            binding.description.ellipsize = TextUtils.TruncateAt.END
        }
        binding.description.setOnClickListener {
            if (isCollapsed) {
                binding.description.maxLines = Integer.MAX_VALUE
            } else {
                binding.description.maxLines = 5
            }
            isCollapsed = !isCollapsed
        }
        binding.ratingAgeLimits.text =
            when (movie.ratingAgeLimits ?: movie.ratingMpaa) {
                "age6" -> "6+"
                "age12" -> "12+"
                "pg13" -> "13+"
                "age16" -> "16+"
                "R" -> "17+"
                "NC-17 " -> "18+"
                "age18" -> "18+"
                else -> " "
            }

        binding.favorite.setOnClickListener {
            lifecycleScope.launch {
                val exists = withContext(Dispatchers.IO) {
                    viewModel.favoriteDao.exists(id)
                }
                withContext(Dispatchers.Main) {
                    if (exists) {
                        withContext(Dispatchers.IO) {
                            viewModel.deleteFavorite()
                        }
                        binding.favorite.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.buttons_color),
                            PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        withContext(Dispatchers.IO) {
                            viewModel.addFavorite()
                        }
                        binding.favorite.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
                            PorterDuff.Mode.MULTIPLY
                        )
                    }
                }
            }
        }


        binding.wantSee.setOnClickListener {
            lifecycleScope.launch {
                val exists = withContext(Dispatchers.IO) {
                    viewModel.wantSeeDao.exists(id)
                }
                withContext(Dispatchers.Main) {
                    if (exists) {
                        withContext(Dispatchers.IO) {
                            viewModel.deleteWantSee()
                        }
                        binding.wantSee.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.buttons_color),
                            PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        withContext(Dispatchers.IO) {
                            viewModel.addWantSee()
                        }
                        binding.wantSee.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
                            PorterDuff.Mode.MULTIPLY
                        )
                    }
                }
            }
        }

        binding.seen.setOnClickListener {
            lifecycleScope.launch {
                val exists = withContext(Dispatchers.IO) {
                    viewModel.seenDao.exists(id)
                }
                withContext(Dispatchers.Main) {
                    if (exists) {
                        withContext(Dispatchers.IO) {
                            viewModel.deleteSeen()
                        }
                        binding.seen.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.buttons_color),
                            PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        withContext(Dispatchers.IO) {
                            viewModel.addSeen()
                        }
                        binding.seen.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
                            PorterDuff.Mode.MULTIPLY
                        )
                    }
                }
            }
        }

        binding.share.setOnClickListener {
            if (movie.imdbId == null) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://www.kinopoisk.ru/film/${movie.kinopoiskId}/"
                )
                startActivity(Intent.createChooser(share, "Share Text"))
            } else {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://www.imdb.com/title/${movie.imdbId}/"
                )
                startActivity(Intent.createChooser(share, "Share Text"))
            }
        }

        binding.menu.setOnClickListener {
            val collectionDialog = CollectionDialog.newInstance(movie)
            collectionDialog.show(childFragmentManager, "CollectionDialog")
        }


    }

    private suspend fun getApiData(newId: Int?, movie: MovieModel) {

        val seasons = viewModel.getSeasons(newId)
        val staff = viewModel.getStaff(newId)
        val gallery = viewModel.getImages(newId, 1)
        val similar = viewModel.getSimilars(newId)

        actorsAdapter.setData(staff)
        limitedActorsAdapter.setLimitedData(staff)
        staffAdapter.setData(staff)
        limitedStaffAdapter.setLimitedData(staff)
        galleryAdapter.setData(gallery)
        similarAdapter.setData(similar)

        if (movie.serial == true) {
            binding.seasons.visibility = View.VISIBLE
            binding.seasonsTitle.visibility = View.VISIBLE
            binding.seasonsInfo.visibility = View.VISIBLE

            binding.actorsTextview.text = getString(R.string.starring_serial)
            binding.staffTextview.text = getString(R.string.staff_serial)

            binding.seasons.text = "$seasons сезон"
            binding.seasonsInfo.text = "$seasons сезонов"
            binding.seasonsTitle.setOnClickListener {
                navigationHandler.navigateMovieToSeasons(viewModel.idState.value)
            }
        }


        binding.actorsAll.text = actorsAdapter.getAllActors().size.toString()
        binding.actorsAllIcon.visibility =
            if (actorsAdapter.getAllActors().size > 20) View.VISIBLE else View.INVISIBLE
        binding.actorsAllIcon.setOnClickListener {
            navigationHandler.navigateMovieToAllActors(
                viewModel.idState.value,
                movie.serial
            )
        }

        binding.staffAll.text = staffAdapter.getAllStaff().size.toString()
        binding.staffAllIcon.visibility =
            if (staffAdapter.getAllStaff().size > 20) View.VISIBLE else View.INVISIBLE
        binding.staffAllIcon.setOnClickListener {
            navigationHandler.navigateMovieToAllStaff(
                viewModel.idState.value,
                movie.serial
            )
        }

        binding.galleryAll.text = viewModel.getTotalImages(newId).toString()
        binding.galleryAllIcon.visibility =
            if (viewModel.getTotalImages(newId) > 20) View.VISIBLE else View.INVISIBLE
        binding.galleryAllIcon.setOnClickListener {
            navigationHandler.navigateMovieToGallery(
                viewModel.idState.value
            )
        }

        binding.similarAll.text = similarAdapter.getAllSimilar().size.toString()
        binding.similarAllIcon.visibility =
            if (similarAdapter.getAllSimilar().size > 20) View.VISIBLE else View.INVISIBLE
        binding.similarAllIcon.setOnClickListener {
            navigationHandler.navigateMovieToSimilar(
                viewModel.idState.value,
                type
            )
        }

        setCategoryVisibility(binding.actorsContainer, limitedActorsAdapter.getAllActors())
        setCategoryVisibility(binding.staffContainer, limitedStaffAdapter.getAllStaff())
        setCategoryVisibility(binding.galleryContainer, galleryAdapter.getAllImages())
        setCategoryVisibility(binding.similarContainer, similarAdapter.getAllSimilar())
    }


    private fun <T> setCategoryVisibility(container: View, data: List<T>) {
        container.visibility = if (data.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun handleHttpException() {
        findNavController().navigateUp()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.title_error))
            .setMessage(getString(R.string.error_description))
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private suspend fun checkStatus() {
        val existsInFavorite = withContext(Dispatchers.IO) {
            viewModel.favoriteDao.exists(viewModel.id)
        }
        val existsInWantSee = withContext(Dispatchers.IO) {
            viewModel.wantSeeDao.exists(viewModel.id)
        }
        val existsInSeen = withContext(Dispatchers.IO) {
            viewModel.seenDao.exists(viewModel.id)
        }
        if (existsInFavorite) {
            binding.favorite.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
                PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.favorite.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.buttons_color),
                PorterDuff.Mode.MULTIPLY
            )
        }

        if (existsInWantSee) {
            binding.wantSee.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
                PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.wantSee.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.buttons_color),
                PorterDuff.Mode.MULTIPLY
            )
        }

        if (existsInSeen) {
            binding.seen.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
                PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.seen.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.buttons_color),
                PorterDuff.Mode.MULTIPLY
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}