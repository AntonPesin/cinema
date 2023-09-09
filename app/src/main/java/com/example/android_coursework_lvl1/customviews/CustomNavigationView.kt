package com.example.android_coursework_lvl1.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.navigation.NavController
import com.example.android_coursework_lvl1.databinding.CustomNavigationViewBinding

class CustomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding: CustomNavigationViewBinding =
        CustomNavigationViewBinding.inflate(LayoutInflater.from(context), this, true)

    val searchIcon = binding.searchIcon
    val homeIcon = binding.homeIcon
    val profileIcon = binding.profileIcon
    private var searchActionId: Int = 0
    private var profileActionId: Int = 0
    private var homeActionId: Int = 0

    private var navController: NavController? = null


    fun setSearchActionId(actionId: Int) {
        searchActionId = actionId
        binding.searchIcon.setOnClickListener {
            navController?.navigate(searchActionId)
        }
    }

    fun setProfileActionId(actionId: Int) {
        profileActionId = actionId
        binding.profileIcon.setOnClickListener {
            navController?.navigate(profileActionId)
        }
    }

    fun setHomeActionId(actionId: Int) {
        homeActionId = actionId
        binding.homeIcon.setOnClickListener {
            navController?.navigate(homeActionId)
        }
    }


    fun setNavController(controller: NavController) {
        navController = controller
    }
}