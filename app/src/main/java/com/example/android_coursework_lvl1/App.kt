package com.example.android_coursework_lvl1

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.android_coursework_lvl1.data.repository.db.FavoriteDataBase
import com.example.android_coursework_lvl1.data.repository.db.FirstCustomDataBase
import com.example.android_coursework_lvl1.data.repository.db.InterestingDataBase
import com.example.android_coursework_lvl1.data.repository.db.SecondCustomDataBase
import com.example.android_coursework_lvl1.data.repository.db.SeenDataBase
import com.example.android_coursework_lvl1.data.repository.db.WantSeeDataBase
import com.example.android_coursework_lvl1.model.enums.CollectionType

class App : Application() {

    companion object {
        lateinit var INSTANCE: App
        private const val FIRST_RUN_DATASTORE = "FIRST_RUN_DATASTORE"
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
                CollectionType.FAVORITE.name
            ).build()

        wantSeeDataBase = Room
            .databaseBuilder(
                this,
                WantSeeDataBase::class.java,
                CollectionType.WANT_SEE.name
            ).fallbackToDestructiveMigration().build()

        seenDataBase = Room
            .databaseBuilder(
                this,
                SeenDataBase::class.java,
                CollectionType.SEEN.name
            ).fallbackToDestructiveMigration().build()

        interestingDataBase = Room
            .databaseBuilder(
                this,
                InterestingDataBase::class.java,
                CollectionType.INTERESTING.name
            ).fallbackToDestructiveMigration().build()

        firstCustomDataBase = Room
            .databaseBuilder(
                this,
                FirstCustomDataBase::class.java,
                CollectionType.FIRST_CUSTOM.name

            ).fallbackToDestructiveMigration().build()

        secondCustomDataBase = Room
            .databaseBuilder(
                this,
                SecondCustomDataBase::class.java,
                CollectionType.SECOND_CUSTOM.name
            ).fallbackToDestructiveMigration().build()


    }

    fun getDatastore(): DataStore<Preferences> {
        return dataStore
    }


}
