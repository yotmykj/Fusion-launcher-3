package com.manas.fusionlauncher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val tileSize: String = "MEDIUM",
    val columnCount: Int = 5,
    val isDarkTheme: Boolean = true,
    val animationSpeed: String = "NORMAL",
    val showSystemApps: Boolean = false,
    val autoArrangeApps: Boolean = true,
    val rowsConfig: String = "favorites,categories,all_apps"
)
