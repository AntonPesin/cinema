package com.example.android_coursework_lvl1.all

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.repository.Repository
import com.example.android_coursework_lvl1.Navigation
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.ActorsAdapter
import com.example.android_coursework_lvl1.adapter.StaffAdapter
import com.example.android_coursework_lvl1.databinding.AllActorsLayoutBinding
import kotlinx.coroutines.launch

class AllStaff : Fragment() {

    private var _binding: AllActorsLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: Repository
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var staffAdapter: StaffAdapter
    private lateinit var navigationHandler: Navigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = AllActorsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = Repository(requireContext())


        val value = requireArguments().getInt(STAFF_ID_KEY)
        val check = arguments?.getString(ACTOR_OR_STAFF_KEY)
        val isMovie = arguments?.getString(MOVIE_OR_SERIAL_KEY)
        Log.d("id", "$value")

        lifecycleScope.launch {
            val staff = repository.getStaff(value)
            if (check == "actor") {
                actorsAdapter = ActorsAdapter()
                binding.staffRv.adapter = actorsAdapter
                actorsAdapter?.setData(staff)
                if (isMovie == "movie") {
                    binding.title.text = getString(R.string.starring_movie)
                } else {
                    binding.title.text = getString(R.string.starring_serial)
                }
            } else {
                staffAdapter = StaffAdapter()
                binding.staffRv.adapter = staffAdapter
                staffAdapter.setData(staff)
                if (isMovie == "movie") {
                    binding.title.text = getString(R.string.staff_movie)
                } else {
                    binding.title.text = getString(R.string.staff_serial)
                }
            }
        }

        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_allStaff_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_allStaff_to_search)
        binding.navigation.setProfileActionId(R.id.action_allStaff_to_profile)
        binding.navigation.setNavController(navController)


        binding.allStaffBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private companion object {
        private const val STAFF_ID_KEY = "staff_id"
        private const val ACTOR_OR_STAFF_KEY = "actor_or_staff"
        private const val MOVIE_OR_SERIAL_KEY = "movie_or_serial"

    }
}
