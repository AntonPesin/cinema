package com.example.android_coursework_lvl1

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.android_coursework_lvl1.db.AppDataBase
import java.util.Locale

class App : Application() {

    lateinit var db: AppDataBase

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        db = Room
            .databaseBuilder(
                applicationContext,
                AppDataBase::class.java,
                "movies_db"
            )
            .fallbackToDestructiveMigration()
            .build()

    }

    companion object {
        lateinit var INSTANCE: App
            private set
    }

}
