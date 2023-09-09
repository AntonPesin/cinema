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
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.LoaderLayoutBinding

class LoaderFragment : Fragment() {


    private lateinit var binding: LoaderLayoutBinding

    private val handler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val loadingTime = 3000
    private val runnable = Runnable {
        findNavController().navigate(R.id.action_loader_to_homepage)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = LoaderLayoutBinding.inflate(inflater, container, false)

        val loader = binding.progressbar
        val vectorDrawable =
            AnimatedVectorDrawableCompat.create(requireContext(), R.drawable.loader_step_1)
        loader.setImageDrawable(vectorDrawable)

        val objectAnimator = ObjectAnimator.ofFloat(loader, "rotation", 0f, 360f)

        objectAnimator.duration = 1000
        objectAnimator.repeatCount = ObjectAnimator.INFINITE
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.start()

        handler.postDelayed(runnable, loadingTime.toLong())


        return binding.root
    }
}