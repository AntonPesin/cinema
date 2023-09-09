package com.example.android_coursework_lvl1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.android_coursework_lvl1.databinding.ActivityMainBinding

private const val PREFERENCE_NAME = "PREFERENCE_NAME"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("is_first_run", true)
        if (isFirstRun) {
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.OnboardingFirst)
        } else {
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.homepage)
        }


    }



    override fun onDestroy() {
        Log.d("onDestroy", "on destroy start")
        super.onDestroy()
        Log.d("super.onDestroy()", "after super.onDestroy()")
        clearSharedPreferences()
        Log.d("clearSharedPreferences()", "after clearSharedPreferences() in ondestroy")
    }

    private fun clearSharedPreferences() {
        Log.d("clearSharedPreferences", "clearSharedPreferences")
        sharedPreferences.edit().clear().apply()
    }



}


