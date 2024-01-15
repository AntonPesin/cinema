package com.example.android_coursework_lvl1.onboarding

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.LoaderLayoutBinding
import com.example.android_coursework_lvl1.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

class LoaderFragment : Fragment() {

    private lateinit var binding: LoaderLayoutBinding
    private lateinit var homeViewModel: HomeViewModel
    private val handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val loadingTime = 3000
    private val runnable = Runnable {
        findNavController().navigate(R.id.action_loader_to_homepage)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LoaderLayoutBinding.inflate(inflater, container, false)


        val vectorDrawable =
            AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.loader)
        binding.progressbar.setImageDrawable(vectorDrawable)

        val objectAnimator = ObjectAnimator.ofFloat(binding.progressbar, "rotation", 0f, 360f)

        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ObjectAnimator.INFINITE
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.start()

        handler.postDelayed(runnable, loadingTime.toLong())
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.isLoading.collect { isLoading ->
                if (!isLoading) {
                    handler.removeCallbacks(runnable)
                    findNavController().navigate(R.id.action_loader_to_homepage)
                }
            }
        }

        return binding.root

    }


}