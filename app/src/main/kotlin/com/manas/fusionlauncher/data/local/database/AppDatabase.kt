package com.manas.fusionlauncher.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.manas.fusionlauncher.data.local.dao.FavoriteAppDao
import com.manas.fusionlauncher.data.local.dao.SettingsDao
import com.manas.fusionlauncher.data.local.entity.FavoriteAppEntity
import com.manas.fusionlauncher.data.local.entity.SettingsEntity

@Database(
    entities = [FavoriteAppEntity::class, SettingsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteAppDao(): FavoriteAppDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fusion_launcher_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
