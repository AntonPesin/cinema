package com.example.android_coursework_lvl1.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.navigation.NavController
import com.example.android_coursework_lvl1.R

class CustomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var homeIcon: ImageView
    private var searchIcon: ImageView
    private var profileIcon: ImageView

    private var searchActionId: Int = 0
    private var profileActionId: Int = 0
    private var homeActionId: Int = 0

    private var navController: NavController? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_navigation_view, this, true)

        homeIcon = findViewById(R.id.home_icon)
        searchIcon = findViewById(R.id.search_icon)
        profileIcon = findViewById(R.id.profile_icon)
    }

    fun setSearchActionId(actionId: Int) {
        searchActionId = actionId
        searchIcon.setOnClickListener {
            navController?.navigate(searchActionId)
        }
    }

    fun setProfileActionId(actionId: Int) {
        profileActionId = actionId
        profileIcon.setOnClickListener {
            navController?.navigate(profileActionId)
        }
    }

    fun setHomeActionId(actionId: Int) {
        homeActionId = actionId
        homeIcon.setOnClickListener {
            navController?.navigate(homeActionId)
        }
    }

    fun setNavController(controller: NavController) {
        navController = controller
    }
}