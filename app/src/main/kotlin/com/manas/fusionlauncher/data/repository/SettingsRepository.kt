package com.manas.fusionlauncher.data.repository

import com.manas.fusionlauncher.data.local.dao.SettingsDao
import com.manas.fusionlauncher.data.local.entity.SettingsEntity
import com.manas.fusionlauncher.domain.model.LauncherSettings
import com.manas.fusionlauncher.domain.model.TileSize
import com.manas.fusionlauncher.domain.model.AnimationSpeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val settingsDao: SettingsDao) {

    fun getSettings(): Flow<LauncherSettings> {
        return settingsDao.getSettings().map { entity ->
            LauncherSettings(
                tileSize = TileSize.valueOf(entity.tileSize),
                columnCount = entity.columnCount,
                isDarkTheme = entity.isDarkTheme,
                animationSpeed = AnimationSpeed.valueOf(entity.animationSpeed),
                showSystemApps = entity.showSystemApps,
                autoArrangeApps = entity.autoArrangeApps,
                rowsConfig = entity.rowsConfig.split(",")
            )
        }
    }

    suspend fun updateSettings(settings: LauncherSettings) {
        val entity = SettingsEntity(
            tileSize = settings.tileSize.name,
            columnCount = settings.columnCount,
            isDarkTheme = settings.isDarkTheme,
            animationSpeed = settings.animationSpeed.name,
            showSystemApps = settings.showSystemApps,
            autoArrangeApps = settings.autoArrangeApps,
            rowsConfig = settings.rowsConfig.joinToString(",")
        )
        settingsDao.updateSettings(entity)
    }

    suspend fun initializeDefaultSettings() {
        val existing = settingsDao.getSettingsSuspend()
        if (existing == null) {
            val defaultSettings = SettingsEntity()
            settingsDao.insertSettings(defaultSettings)
        }
    }

    suspend fun updateTileSize(tileSize: TileSize) {
        val current = settingsDao.getSettingsSuspend() ?: SettingsEntity()
        val updated = current.copy(tileSize = tileSize.name)
        settingsDao.updateSettings(updated)
    }

    suspend fun updateColumnCount(count: Int) {
        val current = settingsDao.getSettingsSuspend() ?: SettingsEntity()
        val updated = current.copy(columnCount = count)
        settingsDao.updateSettings(updated)
    }

    suspend fun updateTheme(isDark: Boolean) {
        val current = settingsDao.getSettingsSuspend() ?: SettingsEntity()
        val updated = current.copy(isDarkTheme = isDark)
        settingsDao.updateSettings(updated)
    }

    suspend fun updateAnimationSpeed(speed: AnimationSpeed) {
        val current = settingsDao.getSettingsSuspend() ?: SettingsEntity()
        val updated = current.copy(animationSpeed = speed.name)
        settingsDao.updateSettings(updated)
    }
}
