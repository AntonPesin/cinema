package com.example.android_coursework_lvl1.ui.onboarding

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.databinding.OnboardingFirstBinding

class OnboardingFirstFragment : Fragment() {

    private lateinit var binding: OnboardingFirstBinding

    companion object{
        private const val PREFERENCE_NAME = "PREFERENCE_NAME"
        private const val IS_FIRST_RUN_KEY = "is_first_run"
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
            add(android.Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                add(android.Manifest.permission.INTERNET)
                add(android.Manifest.permission.ACCESS_NETWORK_STATE)
            }
        }.toTypedArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = OnboardingFirstBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()


        binding.skip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFirst_to_onboardingSecond)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                Toast.makeText(context, getString(R.string.permissions_granted), Toast.LENGTH_SHORT).show()
                updateIsFirstRun(false)
            } else {
                Toast.makeText(context, getString(R.string.permissions_not_granted), Toast.LENGTH_SHORT).show()
            }
        }

    private fun updateIsFirstRun(isFirstRun: Boolean) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(IS_FIRST_RUN_KEY, isFirstRun).apply()
    }


    private fun checkPermissions() {
        val isAllGranted = REQUEST_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted) {
            Toast.makeText(context, getString(R.string.permissions_granted), Toast.LENGTH_SHORT).show()
        } else {
            launcher.launch(REQUEST_PERMISSIONS)
        }
    }

}




