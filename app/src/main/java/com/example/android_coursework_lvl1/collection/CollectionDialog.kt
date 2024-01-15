package com.example.android_coursework_lvl1.collection

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.CollectionsDialogBinding
import com.example.android_coursework_lvl1.models.MovieModel
import com.example.android_coursework_lvl1.viewmodels.ProfileViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectionDialog : DialogFragment() {
    private var _binding: CollectionsDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: ProfileViewModel by viewModels()


    companion object {
        private const val ARG_MOVIE_DATA_JSON = "movieDataJson"

        fun newInstance(movie: MovieModel): CollectionDialog {
            val args = Bundle()
            args.putString(ARG_MOVIE_DATA_JSON, Gson().toJson(movie))
            val fragment = CollectionDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CollectionsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("CollectionsSharedPreference", Context.MODE_PRIVATE)
        val movieModelJson = arguments?.getString(ARG_MOVIE_DATA_JSON)
        val movieModel = Gson().fromJson(movieModelJson, MovieModel::class.java)
        val button = binding.accept.layoutParams as ConstraintLayout.LayoutParams
        val secondCustomCheckBox = binding.secondCustomCheckbox.layoutParams as ConstraintLayout.LayoutParams

        val isFirstCollectionExist = sharedPreferences.getBoolean("isFirstCollectionExist", false)
        val isSecondCollectionExist = sharedPreferences.getBoolean("isSecondCollectionExist", false)

        val firstCollectionName = sharedPreferences.getString("firstCollectionName", "").toString()
        val secondCollectionName = sharedPreferences.getString("secondCollectionName", "").toString()

        val firstCollectionNumbers =
            sharedPreferences.getString("firstCollectionNumbers", "0").toString()
        val secondCollectionNumbers =
            sharedPreferences.getString("secondCollectionNumbers", "0").toString()
        Log.d("isFirstCollectionExist","$isFirstCollectionExist")
        Log.d("isSecondCollectionExist","$isSecondCollectionExist")
        if (!isFirstCollectionExist && !isSecondCollectionExist) {
            binding.firstCustomDivider.visibility = View.GONE
            binding.secondCustomDivider.visibility = View.GONE
            binding.secondCustomName.visibility = View.GONE
            binding.secondCustomCheckbox.visibility = View.GONE
            binding.secondCustomNumbers.visibility = View.GONE
            binding.firstCustomName.visibility = View.GONE
            binding.firstCustomCheckbox.visibility = View.GONE
            binding.firstCustomNumbers.visibility = View.GONE
        }
        if (isFirstCollectionExist) {
            binding.firstCustomName.text = firstCollectionName
            binding.firstCustomNumbers.text = firstCollectionNumbers
        } else {
            binding.firstCustomName.visibility = View.GONE
            binding.firstCustomCheckbox.visibility = View.GONE
            binding.firstCustomNumbers.visibility = View.GONE
            binding.firstCustomDivider.visibility = View.GONE
            secondCustomCheckBox.topToBottom = R.id.add_to_collection_divider
        }
        if (isSecondCollectionExist) {
            binding.secondCustomName.text = secondCollectionName
            binding.secondCustomNumbers.text = secondCollectionNumbers
        } else {
            binding.secondCustomName.visibility = View.GONE
            binding.secondCustomCheckbox.visibility = View.GONE
            binding.secondCustomNumbers.visibility = View.GONE
            binding.secondCustomDivider.visibility = View.GONE
            button.topToBottom = R.id.first_custom_divider
        }

        binding.accept.setOnClickListener {
            if (binding.firstCustomCheckbox.isChecked) {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (movieModel != null) {
                        viewModel.firstCustomDao.insert(movieModel)
                    }
                }
            }
            if (binding.secondCustomCheckbox.isChecked) {
                lifecycleScope.launch(Dispatchers.IO) {
                    if (movieModel != null) {
                        viewModel.secondCustomDao.insert(movieModel)
                    }
                }
            }
            dismiss()
        }

        binding.closeIcon.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
