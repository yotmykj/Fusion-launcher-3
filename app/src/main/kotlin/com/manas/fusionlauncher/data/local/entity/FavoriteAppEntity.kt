package com.manas.fusionlauncher.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_apps")
data class FavoriteAppEntity(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val position: Int
)
