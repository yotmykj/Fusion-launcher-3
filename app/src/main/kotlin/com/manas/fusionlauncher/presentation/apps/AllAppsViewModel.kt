package com.manas.fusionlauncher.presentation.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.manas.fusionlauncher.data.repository.AppRepository
import com.manas.fusionlauncher.data.repository.FavoriteRepository
import com.manas.fusionlauncher.domain.model.AppModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllAppsViewModel(
    private val appRepository: AppRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _allApps = MutableStateFlow<List<AppModel>>(emptyList())
    val allApps: StateFlow<List<AppModel>> = _allApps.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex: StateFlow<Int> = _selectedIndex.asStateFlow()

    init {
        loadApps()
        observeFavorites()
    }

    private fun loadApps() {
        viewModelScope.launch {
            appRepository.getAllApps(includeSystemApps = false).collect { apps ->
                _allApps.value = apps
                _isLoading.value = false
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getAllFavorites().collect { favorites ->
                _favorites.value = favorites.map { it.packageName }.toSet()
            }
        }
    }

    fun toggleFavorite(app: AppModel) {
        viewModelScope.launch {
            if (favoriteRepository.isFavorite(app.packageName)) {
                favoriteRepository.removeFavorite(app.packageName)
            } else {
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
    }

    fun setSelectedIndex(index: Int) {
        _selectedIndex.value = index
    }
}

class AllAppsViewModelFactory(
    private val appRepository: AppRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllAppsViewModel(appRepository, favoriteRepository) as T
    }
}
