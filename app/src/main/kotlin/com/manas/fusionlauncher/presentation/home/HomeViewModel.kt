package com.manas.fusionlauncher.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.manas.fusionlauncher.data.repository.AppRepository
import com.manas.fusionlauncher.data.repository.FavoriteRepository
import com.manas.fusionlauncher.data.repository.SettingsRepository
import com.manas.fusionlauncher.domain.model.AppModel
import com.manas.fusionlauncher.domain.model.LauncherSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val appRepository: AppRepository,
    private val favoriteRepository: FavoriteRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _allApps = MutableStateFlow<List<AppModel>>(emptyList())
    val allApps: StateFlow<List<AppModel>> = _allApps.asStateFlow()

    private val _favorites = MutableStateFlow<List<AppModel>>(emptyList())
    val favorites: StateFlow<List<AppModel>> = _favorites.asStateFlow()

    private val _settings = MutableStateFlow<LauncherSettings?>(null)
    val settings: StateFlow<LauncherSettings?> = _settings.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedAppIndex = MutableStateFlow(0)
    val selectedAppIndex: StateFlow<Int> = _selectedAppIndex.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            settingsRepository.initializeDefaultSettings()
            loadApps()
            loadFavorites()
            observeSettings()
        }
    }

    private fun loadApps() {
        viewModelScope.launch {
            appRepository.getAllApps(includeSystemApps = false).collect { apps ->
                _allApps.value = apps
                _isLoading.value = false
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoriteRepository.getAllFavorites().collect { favorites ->
                val favoriteModels = _allApps.value.filter { app ->
                    favorites.any { it.packageName == app.packageName }
                }
                _favorites.value = favoriteModels.sortedBy { app ->
                    favorites.find { it.packageName == app.packageName }?.position ?: 0
                }
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settings ->
                _settings.value = settings
            }
        }
    }

    fun addToFavorites(app: AppModel) {
        viewModelScope.launch {
            val position = favoriteRepository.getFavoriteCount()
            favoriteRepository.addFavorite(
                com.manas.fusionlauncher.domain.model.FavoriteApp(
                    packageName = app.packageName,
                    appName = app.appName,
                    position = position
                )
            )
        }
    }

    fun removeFromFavorites(packageName: String) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(packageName)
        }
    }

    fun searchApps(query: String) {
        viewModelScope.launch {
            appRepository.searchApps(query).collect { apps ->
                _allApps.value = apps
            }
        }
    }

    fun setSelectedAppIndex(index: Int) {
        _selectedAppIndex.value = index
    }
}

class HomeViewModelFactory(
    private val appRepository: AppRepository,
    private val favoriteRepository: FavoriteRepository,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(appRepository, favoriteRepository, settingsRepository) as T
    }
}
