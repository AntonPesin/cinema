package com.example.android_coursework_lvl1.search

import android.app.AlertDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.SearchSettingsBinding
import com.example.android_coursework_lvl1.navigation.Navigation
import com.example.android_coursework_lvl1.viewmodels.SearchSettingsViewModel
import kotlinx.coroutines.launch


class SearchSettings : Fragment() {
    private var _binding: SearchSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationHandler: Navigation

    private lateinit var yearPickerDialog: YearPickerDialog

    private val viewModel: SearchSettingsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SearchSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countrySpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.countries)
        )
        binding.genreSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.genres)
        )

        lifecycleScope.launch {
            viewModel.searchData.collect { searchData ->
                yearPickerDialog = YearPickerDialog.newInstance(searchData.yearFrom, "yearFrom")
                binding.countrySpinner.setSelection(
                    getIndex(
                        binding.countrySpinner,
                        searchData.country
                    )
                )
                binding.genreSpinner.setSelection(getIndex(binding.genreSpinner, searchData.genre))

                binding.yearFrom.setOnClickListener {
                    showYearPickerDialog( searchData,"yearFrom")
                }

                binding.yearTo.setOnClickListener {
                    showYearPickerDialog( searchData,"yearTo")
                }

                binding.yearFrom.text = searchData.yearFrom.toString()
                binding.yearTo.text = searchData.yearTo.toString()

                binding.ratingSlider.setValues(searchData.ratingFrom.toFloat(), searchData.ratingTo.toFloat())

                updateOrderSelection(searchData.order)
                if (searchData.watched) {
                    searchData.watched = true
                    binding.hideWatchedImageview.setBackgroundResource(R.drawable.seen)
                    binding.hideWatched.text = getString(R.string.show_Watched)
                    binding.hideWatchedImageview.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
                        PorterDuff.Mode.MULTIPLY
                    )
                } else {
                    searchData.watched = false
                    binding.hideWatchedImageview.setBackgroundResource(R.drawable.seen_icon)
                    binding.hideWatched.text = getString(R.string.hide_Watched)
                    binding.hideWatchedImageview.setColorFilter(
                        ContextCompat.getColor(requireContext(), R.color.buttons_color),
                        PorterDuff.Mode.MULTIPLY
                    )
                }

                Log.d("StartHideWatched", "${searchData.watched}")


                binding.settingsAll.setOnClickListener {
                    if (searchData.type != "ALL") {
                        binding.settingsAll.isEnabled = false
                        binding.settingsMovies.isEnabled = true
                        binding.settingsSerial.isEnabled = true
                        searchData.type = "ALL"
                        updateTypeSelection(searchData.type)
                    }
                }

                binding.settingsMovies.setOnClickListener {
                    if (searchData.type != "FILM") {
                        binding.settingsAll.isEnabled = true
                        binding.settingsMovies.isEnabled = false
                        binding.settingsSerial.isEnabled = true
                        searchData.type = "FILM"
                        updateTypeSelection(searchData.type)
                    }
                }

                binding.settingsSerial.setOnClickListener {
                    if (searchData.type != "TV_SERIES") {
                        binding.settingsAll.isEnabled = true
                        binding.settingsMovies.isEnabled = true
                        binding.settingsSerial.isEnabled = false
                        searchData.type = "TV_SERIES"
                        updateTypeSelection(searchData.type)
                    }
                }


                binding.ratingChip.setOnClickListener {
                    if (searchData.order != "RATING") {
                        binding.dateChip.isEnabled = true
                        binding.popularityChip.isEnabled = true
                        binding.ratingChip.isEnabled = false
                        searchData.order = "RATING"
                        updateOrderSelection(searchData.order)
                    }
                }
                binding.popularityChip.setOnClickListener {
                    if (searchData.order != "NUM_VOTE") {
                        binding.dateChip.isEnabled = true
                        binding.ratingChip.isEnabled = true
                        binding.popularityChip.isEnabled = false
                        searchData.order = "NUM_VOTE"
                        updateOrderSelection(searchData.order)
                    }
                }

                binding.dateChip.setOnClickListener {
                    if (searchData.order != "YEAR") {
                        binding.popularityChip.isEnabled = true
                        binding.ratingChip.isEnabled = true
                        binding.dateChip.isEnabled = false
                        searchData.order = "YEAR"
                        updateOrderSelection(searchData.order)
                    }
                }

                binding.hideWatchedImageview.setOnClickListener {
                    if (!searchData.watched) {
                        binding.hideWatched.text = getString(R.string.show_Watched)
                        binding.hideWatchedImageview.setBackgroundResource(R.drawable.seen)
                        binding.hideWatchedImageview.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.skillbox_blue),
                            PorterDuff.Mode.MULTIPLY
                        )
                        searchData.watched = true
                    } else {
                        binding.hideWatchedImageview.setBackgroundResource(R.drawable.seen_icon)
                        binding.hideWatched.text = getString(R.string.hide_Watched)
                        binding.hideWatchedImageview.setColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.buttons_color),
                            PorterDuff.Mode.MULTIPLY
                        )
                        searchData.watched = false
                    }
                    Log.d("hideWatchedImageview", "${searchData.watched}")
                }



                binding.buttonAccept.setOnClickListener {
                    if (searchData.yearFrom > searchData.yearTo) {
                        error()
                    } else {
                        lifecycleScope.launch {
                            viewModel.saveDataToDataStore(
                                searchData.type,
                                binding.countrySpinner.selectedItem.toString(),
                                binding.genreSpinner.selectedItem.toString(),
                                searchData.yearFrom,
                                searchData.yearTo,
                                binding.ratingSlider.values[0].toDouble(),
                                binding.ratingSlider.values[1].toDouble(),
                                searchData.order,
                                searchData.watched
                            )
                        }

                        navigationHandler.navigateSearchSettingsToSearch(
                            searchData.type,
                            binding.countrySpinner.selectedItem.toString(),
                            binding.genreSpinner.selectedItem.toString(),
                            searchData.yearFrom,
                            searchData.yearTo,
                            binding.ratingSlider.values[0].toDouble(),
                            binding.ratingSlider.values[1].toDouble(),
                            searchData.order,
                            searchData.watched
                        )

                    }

                }

            }

        }
        binding.settingsButton.setOnClickListener {
            findNavController().navigateUp()
        }

        navigationHandler = Navigation(findNavController())

    }

    private fun showYearPickerDialog(searchData: SearchSettingsViewModel.SearchData, type: String) {
        yearPickerDialog.setOnYearSetListener { selectedYear, _ ->
            if (type == "yearFrom") {
                binding.yearFrom.text = selectedYear.toString()
                searchData.yearFrom = selectedYear
            } else if (type == "yearTo") {
                binding.yearTo.text = selectedYear.toString()
                searchData.yearTo = selectedYear
            }
        }
        yearPickerDialog.show(childFragmentManager, type)
    }

    private fun getIndex(spinner: Spinner, item: String): Int {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == item) {
                return i
            }
        }
        return 0
    }


    private fun updateTypeSelection(type: String) {

        binding.settingsAll.setChipBackgroundColorResource(if (type == "ALL") R.color.skillbox_blue else R.color.chip_background)
        binding.settingsAll.setTextColor(
            if (type == "ALL") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.settingsMovies.setChipBackgroundColorResource(if (type == "FILM") R.color.skillbox_blue else R.color.chip_background)
        binding.settingsMovies.setTextColor(
            if (type == "FILM") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.settingsSerial.setChipBackgroundColorResource(if (type == "TV_SERIES") R.color.skillbox_blue else R.color.chip_background)
        binding.settingsSerial.setTextColor(
            if (type == "TV_SERIES") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )
    }


    private fun updateOrderSelection(order: String) {

        binding.dateChip.setChipBackgroundColorResource(if (order == "YEAR") R.color.skillbox_blue else R.color.chip_background)
        binding.dateChip.setTextColor(
            if (order == "YEAR") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.popularityChip.setChipBackgroundColorResource(if (order == "NUM_VOTE") R.color.skillbox_blue else R.color.chip_background)
        binding.popularityChip.setTextColor(
            if (order == "NUM_VOTE") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.ratingChip.setChipBackgroundColorResource(if (order == "RATING") R.color.skillbox_blue else R.color.chip_background)
        binding.ratingChip.setTextColor(
            if (order == "RATING") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )
    }

    private fun error() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.title_error))
            .setMessage(getString(R.string.year_error))
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





