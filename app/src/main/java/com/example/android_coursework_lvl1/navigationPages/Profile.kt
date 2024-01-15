package com.example.android_coursework_lvl1.navigationPages

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.viewmodels.SharedViewModel
import com.example.android_coursework_lvl1.adapter.InterestingAdapter
import com.example.android_coursework_lvl1.adapter.SeenAdapter
import com.example.android_coursework_lvl1.databinding.ProfileLayoutBinding
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.navigation.Navigation
import com.example.android_coursework_lvl1.viewmodels.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Profile : Fragment() {
    private var _binding: ProfileLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var seenAdapter: SeenAdapter
    private lateinit var interestingAdapter: InterestingAdapter
    private val viewModel: ProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var navigationHandler: Navigation
    companion object {
        private const val sharedPreferencesName = "CollectionsSharedPreference"

        private const val isFirstCollectionExistKey = "isFirstCollectionExist"
        private const val isSecondCollectionExistKey = "isSecondCollectionExist"

        private const val firstCollectionNameKey = "firstCollectionName"
        private const val secondCollectionNameKey = "secondCollectionName"

        private const val firstCollectionNumbersKey = "firstCollectionNumbers"
        private const val secondCollectionNumbersKey = "secondCollectionNumbers"
    }

    private lateinit var sharedPreferences: SharedPreferences

    private var isFirstCollectionExist = false
    private var isSecondCollectionExist = false

    private var firstCollectionName = ""
    private var secondCollectionName = ""

    private var firstCollectionNumbers = ""
    private var secondCollectionNumbers = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ProfileLayoutBinding.inflate(inflater, container, false)
        lifecycleScope.launch(Dispatchers.IO) {
            val firstCustomSize = viewModel.firstCustomDao.getAll().size.toString()
            val secondCustomSize = viewModel.secondCustomDao.getAll().size.toString()

            isFirstCollectionExist = sharedPreferences.getBoolean(isFirstCollectionExistKey, false)
            isSecondCollectionExist = sharedPreferences.getBoolean(isSecondCollectionExistKey, false)

            firstCollectionName = sharedPreferences.getString(firstCollectionNameKey, "").toString()
            secondCollectionName = sharedPreferences.getString(secondCollectionNameKey, "").toString()

            firstCollectionNumbers = sharedPreferences.getString(firstCollectionNumbersKey, firstCustomSize).toString()
            secondCollectionNumbers = sharedPreferences.getString(secondCollectionNumbersKey, secondCustomSize).toString()

            checkInterestingExistence()
            checkSeenExistence()
            checkCollectionsExistence(firstCustomSize, secondCustomSize)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
        navigationHandler = Navigation(findNavController())
        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteMovies = viewModel.favoriteDao.getAll()
            val wantSeeMovies = viewModel.wantSeeDao.getAll()
            val seenMovies = viewModel.seenDao.getAll()
            val interestingMovies = viewModel.interestingDao.getAll()
            val isFavoriteEmpty = viewModel.favoriteDao.getAll().isEmpty()
            val isWantSeeEmpty = viewModel.wantSeeDao.getAll().isEmpty()
            with(sharedPreferences.edit()) {
                putString(firstCollectionNameKey, firstCollectionName)
                putString(secondCollectionNameKey, secondCollectionName)
                putBoolean(isFirstCollectionExistKey, isFirstCollectionExist)
                putBoolean(isSecondCollectionExistKey, isSecondCollectionExist)
                putString(firstCollectionNumbersKey, firstCollectionNumbers)
                putString(secondCollectionNumbersKey, secondCollectionNumbers)
                apply()
            }

            getCollectionNames()

            withContext(Dispatchers.Main) {

                if (isFavoriteEmpty) {
                    binding.favoriteCollection.numbersBackground.visibility = View.GONE
                    binding.wantSeeCollection.customCollectionNumbers.visibility = View.GONE
                }
                if (isWantSeeEmpty) {
                    binding.wantSeeCollection.numbersBackground.visibility = View.GONE
                    binding.wantSeeCollection.customCollectionNumbers.visibility = View.GONE
                }

                binding.favoriteCollection.customCollectionNumbers.text =
                    favoriteMovies.size.toString()
                binding.favoriteCollection.customCollectionImage.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.favorite, null)
                binding.favoriteCollection.customCollectionName.text = getString(R.string.favorites)

                binding.wantSeeCollection.customCollectionName.text = getString(R.string.want_see)
                binding.wantSeeCollection.customCollectionImage.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.want_see, null)
                binding.wantSeeCollection.customCollectionNumbers.text =
                    wantSeeMovies.size.toString()

                seenAdapter = SeenAdapter(seenMovies)
                binding.seenRv.adapter = seenAdapter
                binding.seenNumbers.text = seenMovies.size.toString()

                interestingAdapter = InterestingAdapter(interestingMovies)
                binding.interestedRv.adapter = interestingAdapter

                seenAdapter.setOnMovieClickListener(object : SeenAdapter.OnMovieClickListener {
                    override fun onMovieClick(movie: MovieModel?) {
                        navigationHandler.navigateProfileToMovie(movie?.kinopoiskId)
                    }
                })

                seenAdapter.showFooter(true)
                seenAdapter.setOnClearClickListener(object : SeenAdapter.OnClearClickListener {
                    override fun onClearClick() {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.seenDao.deleteAll()
                            val movies = viewModel.seenDao.getAll()
                            withContext(Dispatchers.Main) {
                                seenAdapter.updateData(movies)
                            }
                            binding.seenTitle.visibility = View.GONE
                            binding.seenRv.visibility = View.GONE
                            binding.seenTitle.visibility = View.GONE
                            binding.seenNumbers.visibility = View.GONE
                            binding.seenButton.visibility = View.GONE
                        }
                    }

                })

                interestingAdapter.showFooter(true)
                interestingAdapter.setOnMovieClickListener(object :
                    InterestingAdapter.OnMovieClickListener {
                    override fun onMovieClick(movie: MovieModel?) {
                        navigationHandler.navigateProfileToMovie(movie?.kinopoiskId)
                    }
                })

                interestingAdapter.setOnClearClickListener(object :
                    InterestingAdapter.OnClearClickListener {
                    override fun onClearClick() {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.interestingDao.deleteAll()
                            val movies = viewModel.interestingDao.getAll()
                            withContext(Dispatchers.Main) {
                                interestingAdapter.updateData(movies)
                            }
                            binding.interestedTitle.visibility = View.GONE
                            binding.interestedRv.visibility = View.GONE
                        }
                    }

                })

            }

        }

        binding.makeCollectionImageview.setOnClickListener {
            showInputDialog()
        }

        binding.firstCustomCollection.customCollectionButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.firstCustomDao.deleteAll()
            }
            deleteFirstCollection()
            isFirstCollectionExist = false
            sharedPreferences.edit().putBoolean(isFirstCollectionExistKey, false).apply()
        }

        binding.secondCustomCollection.customCollectionButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.secondCustomDao.deleteAll()
            }
            deleteSecondCollection()
            isSecondCollectionExist = false
            sharedPreferences.edit().putBoolean(isSecondCollectionExistKey, false).apply()
        }

        binding.firstCustomCollection.customCollectionImage.setOnClickListener{
            navigationHandler.navigateProfileToCollection(firstCollectionName,true)
        }

        binding.secondCustomCollection.customCollectionImage.setOnClickListener{
            navigationHandler.navigateProfileToCollection(secondCollectionName,false)
        }


        binding.seenButton.setOnClickListener {
            navigationHandler.navigateProfileToCollection(getString(R.string.seen),false)
        }

        binding.favoriteCollection.customCollectionImage.setOnClickListener {
            navigationHandler.navigateProfileToCollection(getString(R.string.favorites),false)
        }

        binding.wantSeeCollection.customCollectionImage.setOnClickListener {
            navigationHandler.navigateProfileToCollection(getString(R.string.want_see),false)
        }


        binding.navigation.homeIcon.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_homepage)
        }

        binding.navigation.searchIcon.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_search)
        }

        binding.navigation.profileIcon.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun checkSeenExistence() {
        lifecycleScope.launch(Dispatchers.IO) {
            val isSeenEmpty = viewModel.seenDao.getAll().isEmpty()

            lifecycleScope.launch(Dispatchers.Main) {
                if (isSeenEmpty) {
                    binding.seenTitle.visibility = View.GONE
                    binding.seenRv.visibility = View.GONE
                    binding.seenTitle.visibility = View.GONE
                    binding.seenNumbers.visibility = View.GONE
                    binding.seenButton.visibility = View.GONE
                } else {
                    binding.seenTitle.visibility = View.VISIBLE
                    binding.seenRv.visibility = View.VISIBLE
                    binding.seenTitle.visibility = View.VISIBLE
                    binding.seenNumbers.visibility = View.VISIBLE
                    binding.seenButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun checkInterestingExistence() {
        lifecycleScope.launch(Dispatchers.IO) {
            val isInterestingEmpty = viewModel.interestingDao.getAll().isEmpty()
            lifecycleScope.launch(Dispatchers.Main) {
                if (isInterestingEmpty) {
                    binding.interestedTitle.visibility = View.GONE
                    binding.interestedRv.visibility = View.GONE
                } else {
                    binding.interestedTitle.visibility = View.VISIBLE
                    binding.interestedRv.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun showInputDialog() {
        if (isFirstCollectionExist && isSecondCollectionExist) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.title_error))
                .setMessage(getString(R.string.collection_error))
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
        } else {
            val editText = EditText(requireContext())
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.enter_title))
                .setView(editText)
                .setPositiveButton("OK") { _, _ ->
                    val enteredText = editText.text.toString()
                    if (enteredText.isNotBlank()) {
                        createCollection(enteredText)
                    } else {
                        val errorBuilder = AlertDialog.Builder(requireContext())
                        errorBuilder.setTitle(getString(R.string.title_error))
                            .setMessage(getString(R.string.input_error))
                            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                            .show()
                    }
                }
                .setNegativeButton(getString(R.string.cancel_button), null)
                .create()
            dialog.show()
        }
    }


    private fun createCollection(collectionName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val firstCustomSize = viewModel.firstCustomDao.getAll().size.toString()
            val secondCustomSize = viewModel.secondCustomDao.getAll().size.toString()
            lifecycleScope.launch(Dispatchers.Main) {
                if (!isFirstCollectionExist) {
                    binding.firstCustomCollection.customCollectionNumbers.text = firstCustomSize
                    binding.firstCustomCollection.customCollectionName.text = collectionName
                    setFirstCollection()
                    isFirstCollectionExist = true
                    sharedPreferences.edit().putBoolean(isFirstCollectionExistKey, true).apply()
                    sharedPreferences.edit().putString(firstCollectionNameKey, collectionName)
                        .apply()
                } else if (!isSecondCollectionExist) {
                    binding.secondCustomCollection.customCollectionNumbers.text = secondCustomSize
                    binding.secondCustomCollection.customCollectionName.text = collectionName
                    setSecondCollection()
                    isSecondCollectionExist = true
                    sharedPreferences.edit().putBoolean(isSecondCollectionExistKey, true).apply()
                    sharedPreferences.edit().putString(secondCollectionNameKey, collectionName).apply()
                }
            }
        }
    }

    private fun checkCollectionsExistence(firstCustomSize: String, secondCustomSize: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            lifecycleScope.launch(Dispatchers.Main) {
                if (isFirstCollectionExist) {
                    binding.firstCustomCollection.customCollectionName.text = firstCollectionName
                    binding.firstCustomCollection.customCollectionNumbers.text = firstCustomSize
                    sharedPreferences.edit().putString(firstCollectionNumbersKey, firstCustomSize)
                        .apply()
                    setFirstCollection()
                } else {
                    deleteFirstCollection()
                }

                if (isSecondCollectionExist) {
                    binding.secondCustomCollection.customCollectionName.text = secondCollectionName
                    binding.secondCustomCollection.customCollectionNumbers.text = secondCustomSize
                    sharedPreferences.edit().putString(secondCollectionNumbersKey, secondCustomSize)
                        .apply()
                    setSecondCollection()
                } else {
                    deleteSecondCollection()
                }
            }
        }
    }

    private fun setFirstCollection() {
        binding.firstCustomCollection.numbersBackground.visibility = View.VISIBLE
        binding.firstCustomCollection.customCollectionImage.visibility = View.VISIBLE
        binding.firstCustomCollection.customCollectionButton.visibility = View.VISIBLE
        binding.firstCustomCollection.frame.visibility = View.VISIBLE
        binding.firstCustomCollection.customCollectionName.visibility = View.VISIBLE
    }

    private fun deleteFirstCollection() {
        binding.firstCustomCollection.customCollectionImage.visibility = View.GONE
        binding.firstCustomCollection.numbersBackground.visibility = View.GONE
        binding.firstCustomCollection.customCollectionButton.visibility = View.GONE
        binding.firstCustomCollection.frame.visibility = View.GONE
        binding.firstCustomCollection.customCollectionName.visibility = View.GONE
    }

    private fun setSecondCollection() {
        binding.secondCustomCollection.numbersBackground.visibility = View.VISIBLE
        binding.secondCustomCollection.customCollectionImage.visibility = View.VISIBLE
        binding.secondCustomCollection.customCollectionButton.visibility = View.VISIBLE
        binding.secondCustomCollection.frame.visibility = View.VISIBLE
        binding.secondCustomCollection.customCollectionName.visibility = View.VISIBLE
    }

    private fun deleteSecondCollection() {
        binding.secondCustomCollection.customCollectionImage.visibility = View.GONE
        binding.secondCustomCollection.numbersBackground.visibility = View.GONE
        binding.secondCustomCollection.customCollectionButton.visibility = View.GONE
        binding.secondCustomCollection.frame.visibility = View.GONE
        binding.secondCustomCollection.customCollectionName.visibility = View.GONE
    }

    private fun getCollectionNames(): List<String> {
        val defaultCollections = mutableListOf<String>()
        if (isFirstCollectionExist) {
            defaultCollections.add(firstCollectionName)
        }

        if (isSecondCollectionExist) {
            Log.d("getCollectionNames",  secondCollectionName)
            defaultCollections.add(secondCollectionName)
        }
        sharedViewModel.updateCollections(defaultCollections)
        return defaultCollections
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
