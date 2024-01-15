package com.example.android_coursework_lvl1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android_coursework_lvl1.App
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    application: Application,
) : AndroidViewModel(application) {

    val favoriteDao = App.INSTANCE.favoriteDataBase.favoriteDao()
    val wantSeeDao = App.INSTANCE.wantSeeDataBase.wantSeeDao()
    val seenDao = App.INSTANCE.seenDataBase.seenDao()
    val interestingDao = App.INSTANCE.interestingDataBase.interestingDao()
    val firstCustomDao = App.INSTANCE.firstCustomDataBase.firstCustomDao()
    val secondCustomDao = App.INSTANCE.secondCustomDataBase.secondCustomDao()


}