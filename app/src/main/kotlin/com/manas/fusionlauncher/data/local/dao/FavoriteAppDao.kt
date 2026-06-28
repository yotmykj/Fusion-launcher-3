package com.manas.fusionlauncher.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.manas.fusionlauncher.data.local.entity.FavoriteAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAppDao {
    @Query("SELECT * FROM favorite_apps ORDER BY position ASC")
    fun getAllFavorites(): Flow<List<FavoriteAppEntity>>

    @Query("SELECT * FROM favorite_apps WHERE packageName = :packageName")
    suspend fun getFavoriteByPackage(packageName: String): FavoriteAppEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteAppEntity)

    @Update
    suspend fun updateFavorite(favorite: FavoriteAppEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteAppEntity)

    @Query("DELETE FROM favorite_apps WHERE packageName = :packageName")
    suspend fun deleteFavoriteByPackage(packageName: String)

    @Query("SELECT COUNT(*) FROM favorite_apps")
    suspend fun getFavoriteCount(): Int
}
