package com.example.android_coursework_lvl1.pages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.android_coursework_lvl1.R
import com.example.android_coursework_lvl1.adapter.ActorsAdapter
import com.example.android_coursework_lvl1.data.Repository
import com.example.android_coursework_lvl1.databinding.PersonpageLayoutBinding
import com.example.android_coursework_lvl1.models.StaffModel
import com.example.android_coursework_lvl1.navigation.Navigation
import kotlinx.coroutines.launch

class ActorPage : Fragment() {

    private var _binding: PersonpageLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var actorsAdapter: ActorsAdapter
    private lateinit var navigationHandler: Navigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PersonpageLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        binding.navigation.setHomeActionId(R.id.action_personPage_to_homepage)
        binding.navigation.setSearchActionId(R.id.action_personPage_to_search)
        binding.navigation.setProfileActionId(R.id.action_personPage_to_profile)
        binding.navigation.setNavController(navController)

        navigationHandler = Navigation(findNavController())
        actorsAdapter = ActorsAdapter()

        val id = arguments?.getInt("id")

        lifecycleScope.launch {
            id?.let {
                val data = Repository(requireContext()).getActor(id)

                binding.name.text = data.nameRu ?: data.nameEn
                binding.professionInfo.text = data.profession
                binding.sexInfo.text = when (data.sex) {
                    "MALE" -> getString(R.string.male)
                    "FEMALE" -> getString(R.string.female)
                    else -> " "
                }

                if (data.age == 0) {
                    binding.ageCategory.visibility = View.GONE
                    binding.ageInfo.visibility = View.GONE
                } else {
                    binding.ageCategory.visibility = View.VISIBLE
                    binding.ageInfo.visibility = View.VISIBLE
                    binding.ageInfo.text = data.age.toString()
                }

                setCategoryVisibility(binding.birthdayCategory, !data.birthday.isNullOrBlank())
                binding.birthdayInfo.text = data.birthday


                setCategoryVisibility(binding.deathCategory, !data.death.isNullOrBlank())
                binding.deathInfo.text = data.death

                Glide.with(requireContext())
                    .load(data.posterUrl)
                    .into(binding.photo)


                binding.bestRv.adapter
            }
        }

        actorsAdapter.setOnActorsClickListener(object :
            ActorsAdapter.ActorsClickListener {
            override fun onActorClick(staff: StaffModel?) {
                Log.d(" actor ID", "${staff?.staffId}")
                navigationHandler.navigateAllStaffToActor(staff?.staffId)
            }
        })



        binding.personBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setCategoryVisibility(view: View, isVisible: Boolean) {
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}