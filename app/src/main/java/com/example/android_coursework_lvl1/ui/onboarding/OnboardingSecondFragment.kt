package com.example.android_coursework_lvl1.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.OnboardingSecondBinding

class OnboardingSecondFragment : Fragment() {

    private lateinit var binding: OnboardingSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = OnboardingSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.skip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingSecond_to_onboardingThird)
        }
    }
}