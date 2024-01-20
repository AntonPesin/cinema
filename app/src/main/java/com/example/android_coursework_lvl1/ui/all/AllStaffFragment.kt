package com.example.android_coursework_lvl1.ui.all

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.ui.adapters.ActorsAdapter
import com.example.android_coursework_lvl1.ui.adapters.StaffAdapter
import com.example.android_coursework_lvl1.data.repository.Repository
import com.example.android_coursework_lvl1.data.repository.network.models.StaffModel
import com.example.android_coursework_lvl1.databinding.AllStaffBinding
import com.example.android_coursework_lvl1.ui.navigation.Navigation
import kotlinx.coroutines.launch

class AllStaffFragment : Fragment() {

    private var _binding: AllStaffBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: Repository
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var staffAdapter: StaffAdapter
    private lateinit var navigationHandler: Navigation

    private companion object {
        private const val STAFF_ID_KEY = "staff_id"
        private const val ACTOR_OR_STAFF_KEY = "actor_or_staff"
        private const val TYPE_KEY = "type"
        private const val ACTOR_KEY = "actor"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = AllStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = Repository(requireContext())
        navigationHandler = Navigation(findNavController())
        val value = requireArguments().getInt(STAFF_ID_KEY)
        val check = arguments?.getString(ACTOR_OR_STAFF_KEY)
        val isSerial = arguments?.getBoolean(TYPE_KEY)
        Log.d("id", "$value")

        lifecycleScope.launch {
            val staff = repository.getStaff(value)
            if (check == ACTOR_KEY) {
                actorsAdapter = ActorsAdapter()
                binding.staffRv.adapter = actorsAdapter
                actorsAdapter.setData(staff)
                if (!isSerial!!) {
                    binding.allStaffTitle.text = getString(R.string.starring_movie)
                } else {
                    binding.allStaffTitle.text = getString(R.string.starring_serial)
                }

                actorsAdapter.setOnActorsClickListener(
                    object : ActorsAdapter.ActorsClickListener {
                        override fun onActorClick(staff: StaffModel?) {
                            Log.d("actor_ID", "${staff?.staffId}")
                            navigationHandler.navigateAllStaffToActor(staff?.staffId)
                        }
                    }
                )
            } else {
                staffAdapter = StaffAdapter()
                binding.staffRv.adapter = staffAdapter
                staffAdapter.setData(staff)
                if (!isSerial!!) {
                    binding.allStaffTitle.text = getString(R.string.staff_movie)
                } else {
                    binding.allStaffTitle.text = getString(R.string.staff_serial)
                }
                staffAdapter.setOnStaffClickListener(
                    object : StaffAdapter.StaffClickListener {
                        override fun onStaffClick(staff: StaffModel?) {
                            Log.d("staff_ID", "${staff?.staffId}")
                            navigationHandler.navigateAllStaffToActor(staff?.staffId)
                        }
                    }
                )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
