package com.manas.fusionlauncher.domain.model

import android.graphics.drawable.Drawable

data class AppModel(
    val packageName: String,
    val appName: String,
    val icon: Drawable?,
    val isSystemApp: Boolean = false,
    val installTime: Long = 0,
    val lastUpdateTime: Long = 0
)
