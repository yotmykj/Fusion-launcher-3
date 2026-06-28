package com.manas.fusionlauncher

import android.app.Application
import com.manas.fusionlauncher.data.local.database.AppDatabase

class FusionLauncherApp : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        
        // Initialize database
        database = AppDatabase.getDatabase(this)
    }
}
