package com.example.android_coursework_lvl1


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.android_coursework_lvl1.databinding.ActivityMainBinding
import com.example.android_coursework_lvl1.viewmodels.SearchSettingsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchSettingsViewModel : SearchSettingsViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchSettingsViewModel = ViewModelProvider(this)[SearchSettingsViewModel::class.java]
        sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        lifecycleScope.launch {
            val isFirstRun = readIsFirstRunFromDataStore()
            if (isFirstRun) {
                val navController = findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.OnboardingFirst)
                writeIsFirstRunToDataStore(false)
            } else {
                val navController = findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.loader)
            }


            searchSettingsViewModel.clearDataStore()
        }
    }


    private suspend fun readIsFirstRunFromDataStore(): Boolean {
        return App.INSTANCE.getDatastore().data.first()[IS_FIRST_RUN_KEY] ?: true
    }

    private suspend fun writeIsFirstRunToDataStore(value: Boolean) {
        App.INSTANCE.getDatastore().edit { preferences ->
            preferences[IS_FIRST_RUN_KEY] = value
        }
    }

    override fun onStop() {
        Log.d("onDestroy", "on destroy start")
        super.onStop()
        Log.d("super.onDestroy()", "after super.onDestroy()")
        clearSharedPreferences()
    }

    private fun clearSharedPreferences() {
        Log.d("clearSharedPreferences", "clearSharedPreferences")
        sharedPreferences.edit().clear().apply()
    }


    companion object {
        private val IS_FIRST_RUN_KEY = booleanPreferencesKey("is_first_run")
        private const val PREFERENCE_NAME = "PREFERENCE_NAME"
    }

}


