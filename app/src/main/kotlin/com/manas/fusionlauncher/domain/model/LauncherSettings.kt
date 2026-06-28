package com.manas.fusionlauncher.domain.model

data class LauncherSettings(
    val tileSize: TileSize = TileSize.MEDIUM,
    val columnCount: Int = 5,
    val isDarkTheme: Boolean = true,
    val animationSpeed: AnimationSpeed = AnimationSpeed.NORMAL,
    val showSystemApps: Boolean = false,
    val autoArrangeApps: Boolean = true,
    val rowsConfig: List<String> = listOf("favorites", "categories", "all_apps")
)

enum class TileSize {
    SMALL, MEDIUM, LARGE
}

enum class AnimationSpeed {
    SLOW, NORMAL, FAST
}
