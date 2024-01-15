package com.example.android_coursework_lvl1

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.android_coursework_lvl1.db.FavoriteDataBase
import com.example.android_coursework_lvl1.db.FirstCustomDataBase
import com.example.android_coursework_lvl1.db.InterestingDataBase
import com.example.android_coursework_lvl1.db.SecondCustomDataBase
import com.example.android_coursework_lvl1.db.SeenDataBase
import com.example.android_coursework_lvl1.db.WantSeeDataBase


class App : Application() {

    companion object {
        lateinit var INSTANCE: App
        private const val FIRST_RUN_DATASTORE = "FIRST_RUN_DATASTORE"
        private const val FAVORITE = "FAVORITE"
        private const val WANT_SEE = "WANT_SEE"
        private const val SEEN = "SEEN"
        private const val INTERESTING = "INTERESTING"
        private const val FIRST_CUSTOM = "FIRST_CUSTOM"
        private const val SECOND_CUSTOM = "SECOND_CUSTOM"
    }

    private val Context.dataStore by preferencesDataStore(name = FIRST_RUN_DATASTORE)



    lateinit var favoriteDataBase: FavoriteDataBase
    lateinit var wantSeeDataBase: WantSeeDataBase
    lateinit var seenDataBase: SeenDataBase
    lateinit var interestingDataBase: InterestingDataBase
    lateinit var firstCustomDataBase: FirstCustomDataBase
    lateinit var secondCustomDataBase: SecondCustomDataBase

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        favoriteDataBase = Room
            .databaseBuilder(
                this,
                FavoriteDataBase::class.java,
                FAVORITE
            ).build()

        wantSeeDataBase = Room
            .databaseBuilder(
                this,
                WantSeeDataBase::class.java,
                WANT_SEE
            ).fallbackToDestructiveMigration().build()

        seenDataBase = Room
            .databaseBuilder(
                this,
                SeenDataBase::class.java,
                SEEN
            ).fallbackToDestructiveMigration().build()

        interestingDataBase = Room
            .databaseBuilder(
                this,
                InterestingDataBase::class.java,
                INTERESTING
            ).fallbackToDestructiveMigration().build()

        firstCustomDataBase = Room
            .databaseBuilder(
                this,
                FirstCustomDataBase::class.java,
                FIRST_CUSTOM
            ).fallbackToDestructiveMigration().build()

        secondCustomDataBase = Room
            .databaseBuilder(
                this,
                SecondCustomDataBase::class.java,
                SECOND_CUSTOM
            ).fallbackToDestructiveMigration().build()


    }

    fun getDatastore(): DataStore<Preferences> {
        return dataStore
    }


}
