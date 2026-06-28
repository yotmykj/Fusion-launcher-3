package com.manas.fusionlauncher.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.manas.fusionlauncher.data.repository.SettingsRepository
import com.manas.fusionlauncher.domain.model.AnimationSpeed
import com.manas.fusionlauncher.domain.model.LauncherSettings
import com.manas.fusionlauncher.domain.model.TileSize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _settings = MutableStateFlow<LauncherSettings?>(null)
    val settings: StateFlow<LauncherSettings?> = _settings.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settings ->
                _settings.value = settings
            }
        }
    }

    fun updateTileSize(tileSize: TileSize) {
        viewModelScope.launch {
            settingsRepository.updateTileSize(tileSize)
        }
    }

    fun updateColumnCount(count: Int) {
        viewModelScope.launch {
            settingsRepository.updateColumnCount(count)
        }
    }

    fun updateTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateTheme(isDark)
        }
    }

    fun updateAnimationSpeed(speed: AnimationSpeed) {
        viewModelScope.launch {
            settingsRepository.updateAnimationSpeed(speed)
        }
    }

    fun updateSettings(settings: LauncherSettings) {
        viewModelScope.launch {
            settingsRepository.updateSettings(settings)
        }
    }
}

class SettingsViewModelFactory(
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(settingsRepository) as T
    }
}
