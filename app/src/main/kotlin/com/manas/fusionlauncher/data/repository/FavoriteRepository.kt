package com.manas.fusionlauncher.data.repository

import com.manas.fusionlauncher.data.local.dao.FavoriteAppDao
import com.manas.fusionlauncher.data.local.entity.FavoriteAppEntity
import com.manas.fusionlauncher.domain.model.FavoriteApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepository(private val favoriteAppDao: FavoriteAppDao) {

    fun getAllFavorites(): Flow<List<FavoriteApp>> {
        return favoriteAppDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                FavoriteApp(
                    packageName = entity.packageName,
                    appName = entity.appName,
                    position = entity.position
                )
            }
        }
    }

    suspend fun addFavorite(favoriteApp: FavoriteApp) {
        val entity = FavoriteAppEntity(
            packageName = favoriteApp.packageName,
            appName = favoriteApp.appName,
            position = favoriteApp.position
        )
        favoriteAppDao.insertFavorite(entity)
    }

    suspend fun removeFavorite(packageName: String) {
        favoriteAppDao.deleteFavoriteByPackage(packageName)
    }

    suspend fun isFavorite(packageName: String): Boolean {
        return favoriteAppDao.getFavoriteByPackage(packageName) != null
    }

    suspend fun updateFavoritePosition(packageName: String, newPosition: Int) {
        val favorite = favoriteAppDao.getFavoriteByPackage(packageName)
        favorite?.let {
            val updated = it.copy(position = newPosition)
            favoriteAppDao.updateFavorite(updated)
        }
    }

    suspend fun getFavoriteCount(): Int {
        return favoriteAppDao.getFavoriteCount()
    }
}
