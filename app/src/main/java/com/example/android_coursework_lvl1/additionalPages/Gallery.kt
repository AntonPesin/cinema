package com.example.android_coursework_lvl1.additionalPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.Navigation
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.ActorsAdapter
import com.example.android_coursework_lvl1.adapter.GalleryAdapter
import com.example.android_coursework_lvl1.adapter.StaffAdapter
import com.example.android_coursework_lvl1.databinding.GalleryLayoutBinding
import com.example.android_coursework_lvl1.models.ImageModel
import com.example.android_coursework_lvl1.models.StaffModel
import com.example.android_coursework_lvl1.pages.ImagePage
import com.example.android_coursework_lvl1.repository.Repository
import kotlinx.coroutines.launch

class Gallery : Fragment() {

    private var _binding: GalleryLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var navigationHandler: Navigation
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var repository: Repository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = GalleryLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = Repository(requireContext())
        navigationHandler = Navigation(findNavController())

        val textColorEnabled = ContextCompat.getColor(requireContext(), R.color.white)
        val textColorDisabled = ContextCompat.getColor(requireContext(), R.color.black)

        val id = arguments?.getInt("id")
        Log.d("id_gallery","$id")
        galleryAdapter = GalleryAdapter()
        lifecycleScope.launch {
            val images =  repository.getStillImage(id)
            galleryAdapter.setData(images)
            binding.galleryRv.adapter = galleryAdapter
        }

        binding.still.setOnClickListener {
            if (binding.still.isEnabled) {
                binding.shooting.isEnabled = true
                binding.posters.isEnabled = true
                binding.still.isEnabled = false
                binding.still.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_enabled)
                binding.stillTextview.setTextColor(textColorEnabled)
                binding.shooting.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_disabled)
                binding.shootingTextview.setTextColor(textColorDisabled)
                binding.posters.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_disabled)
                binding.postersTextview.setTextColor(textColorDisabled)

                lifecycleScope.launch {
                    val images = repository.getStillImage(id!!)
                    galleryAdapter.setData(images)
                    binding.galleryRv.adapter = galleryAdapter
                }
            }
        }

        binding.shooting.setOnClickListener {
            if (binding.shooting.isEnabled) {
                binding.shooting.isEnabled = false
                binding.posters.isEnabled = true
                binding.still.isEnabled = true
                binding.shooting.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_enabled)
                binding.shootingTextview.setTextColor(textColorEnabled)
                binding.still.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_disabled)
                binding.stillTextview.setTextColor(textColorDisabled)
                binding.posters.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_disabled)
                binding.postersTextview.setTextColor(textColorDisabled)

                lifecycleScope.launch{
                    val images = repository.getShootingImage(id)
                    galleryAdapter.setData(images)
                    binding.galleryRv.adapter = galleryAdapter
                }
            }
        }

        binding.posters.setOnClickListener {
            if (binding.posters.isEnabled) {
                binding.shooting.isEnabled = true
                binding.posters.isEnabled = false
                binding.still.isEnabled = true
                binding.posters.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_enabled)
                binding.postersTextview.setTextColor(textColorEnabled)
                binding.still.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_disabled)
                binding.stillTextview.setTextColor(textColorDisabled)
                binding.shooting.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.gallery_buttons_disabled)
                binding.shootingTextview.setTextColor(textColorDisabled)
                lifecycleScope.launch{
                    val images = repository.getPosterImage(id)
                    galleryAdapter.setData(images)
                    binding.galleryRv.adapter = galleryAdapter
                }
            }
        }

        lifecycleScope.launch {
            binding.stillNumber.text = id?.let { repository.getTotalStillImage(it).toString() }
            binding.shootingNumber.text =
                id?.let { repository.getShootingImage(it).size.toString() }
            binding.postersNumber.text =
                id?.let { repository.getPosterImage(it).size.toString() }
        }

        binding.galleryIconBack.setOnClickListener {
            findNavController().navigateUp()
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





        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_gallery_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_gallery_to_search)
        binding.navigation.setProfileActionId(R.id.action_gallery_to_profile)
        binding.navigation.setNavController(navController)
    }
}