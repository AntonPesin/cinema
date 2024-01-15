package com.example.android_coursework_lvl1.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.GalleryAdapter
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.databinding.GalleryLayoutBinding
import com.example.android_coursework_lvl1.models.ImageModel
import com.example.android_coursework_lvl1.navigation.Navigation
import com.example.android_coursework_lvl1.viewmodels.GalleryViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GalleryPage : Fragment() {
    private var _binding: GalleryLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationHandler: Navigation
    private lateinit var repository: Repository
    private lateinit var galleryAdapter: GalleryAdapter
    private val viewModel: GalleryViewModel by viewModels()
    private var currentCategory: String = "still"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = GalleryLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryAdapter = GalleryAdapter()
        repository = Repository(requireContext())
        navigationHandler = Navigation(findNavController())

        val id = viewModel.id
        binding.galleryRv.adapter = galleryAdapter

        lifecycleScope.launch {
            val stillImageCount = repository.getTotalStillImage(id)
            val shootingImageCount = repository.getTotalShootingImage(id)
            val posterImageCount = repository.getTotalPosterImage(id)

            setCategoryVisibility(binding.still, stillImageCount > 0)
            setCategoryVisibility(binding.shooting, shootingImageCount > 0)
            setCategoryVisibility(binding.posters, posterImageCount > 0)

            binding.still.text = getString(R.string.still) + " " + stillImageCount
            binding.shooting.text = getString(R.string.shooting) + " " + shootingImageCount
            binding.posters.text = getString(R.string.posters) + " " + posterImageCount

            if (stillImageCount > 0) {
                viewModel.pagedStillImages.onEach {
                    currentCategory = "still"
                    galleryAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            } else if (shootingImageCount > 0) {
                viewModel.pagedShootingImages.onEach {
                    currentCategory = "shooting"
                    galleryAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            } else if (posterImageCount > 0) {
                viewModel.pagedPosterImages.onEach {
                    currentCategory = "poster"
                    galleryAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        binding.still.setOnClickListener {
            if (currentCategory != "still") {
                currentCategory = "still"

                binding.shooting.isEnabled = true
                binding.posters.isEnabled = true
                binding.still.isEnabled = false

                updateCategorySelection()

                viewModel.pagedStillImages.onEach {
                    galleryAdapter.submitData(it)
                    binding.galleryRv.adapter = galleryAdapter
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        binding.shooting.setOnClickListener {
            if (currentCategory != "shooting") {
                currentCategory = "shooting"

                binding.shooting.isEnabled = false
                binding.posters.isEnabled = true
                binding.still.isEnabled = true

                updateCategorySelection()

                viewModel.pagedShootingImages.onEach {
                    galleryAdapter.submitData(it)
                    binding.galleryRv.adapter = galleryAdapter
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        binding.posters.setOnClickListener {
            if (currentCategory != "poster") {
                currentCategory = "poster"

                binding.shooting.isEnabled = true
                binding.posters.isEnabled = false
                binding.still.isEnabled = true

                updateCategorySelection()

                viewModel.pagedPosterImages.onEach {
                    galleryAdapter.submitData(it)
                    binding.galleryRv.adapter = galleryAdapter
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        galleryAdapter.setOnImageClickListener(object :
            GalleryAdapter.ImageClickListener {
            override fun onImageClick(image: ImageModel?) {
                val imageUrls = galleryAdapter.snapshot().items.map { it.imageUrl }
                val currentPosition = galleryAdapter.snapshot().items.indexOf(image)
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

        binding.galleryIconBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_gallery_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_gallery_to_search)
        binding.navigation.setProfileActionId(R.id.action_gallery_to_profile)
        binding.navigation.setNavController(navController)
    }

    private fun setCategoryVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun updateCategorySelection() {
        binding.still.setChipBackgroundColorResource(if (currentCategory == "still") R.color.skillbox_blue else R.color.chip_background)
        binding.still.setTextColor(
            if (currentCategory == "still") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.shooting.setChipBackgroundColorResource(if (currentCategory == "shooting") R.color.skillbox_blue else R.color.chip_background)
        binding.shooting.setTextColor(
            if (currentCategory == "shooting") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )

        binding.posters.setChipBackgroundColorResource(if (currentCategory == "poster") R.color.skillbox_blue else R.color.chip_background)
        binding.posters.setTextColor(
            if (currentCategory == "poster") resources.getColor(R.color.white) else resources.getColor(
                R.color.textColor
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}