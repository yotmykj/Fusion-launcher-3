package com.manas.fusionlauncher.domain.model

data class AppCategory(
    val id: String,
    val name: String,
    val icon: String,
    val apps: List<AppModel> = emptyList()
)

object AppCategories {
    const val GAMES = "games"
    const val ENTERTAINMENT = "entertainment"
    const val PRODUCTIVITY = "productivity"
    const val TOOLS = "tools"
    const val MUSIC = "music"
    const val NEWS = "news"
    const val SOCIAL = "social"
    const val SPORTS = "sports"
}
