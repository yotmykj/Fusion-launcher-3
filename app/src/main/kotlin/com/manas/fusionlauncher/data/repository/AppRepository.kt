package com.manas.fusionlauncher.data.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.manas.fusionlauncher.domain.model.AppModel
import com.manas.fusionlauncher.domain.model.AppCategories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class AppRepository(private val context: Context) {
    private val packageManager = context.packageManager

    suspend fun getAllApps(includeSystemApps: Boolean = false): Flow<List<AppModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val apps = mutableListOf<AppModel>()
                val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

                installedApps.forEach { appInfo ->
                    if (shouldIncludeApp(appInfo, includeSystemApps)) {
                        val appModel = AppModel(
                            packageName = appInfo.packageName,
                            appName = appInfo.loadLabel(packageManager).toString(),
                            icon = appInfo.loadIcon(packageManager),
                            isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                            installTime = appInfo.firstInstallTime,
                            lastUpdateTime = appInfo.lastUpdateTime
                        )
                        apps.add(appModel)
                    }
                }

                apps.sortBy { it.appName }
                flowOf(apps)
            } catch (e: Exception) {
                flowOf(emptyList())
            }
        }
    }

    suspend fun searchApps(query: String, includeSystemApps: Boolean = false): Flow<List<AppModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val apps = mutableListOf<AppModel>()
                val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                val searchTerm = query.lowercase()

                installedApps.forEach { appInfo ->
                    if (shouldIncludeApp(appInfo, includeSystemApps)) {
                        val appName = appInfo.loadLabel(packageManager).toString().lowercase()
                        if (appName.contains(searchTerm) || appInfo.packageName.contains(searchTerm)) {
                            val appModel = AppModel(
                                packageName = appInfo.packageName,
                                appName = appName,
                                icon = appInfo.loadIcon(packageManager),
                                isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                                installTime = appInfo.firstInstallTime,
                                lastUpdateTime = appInfo.lastUpdateTime
                            )
                            apps.add(appModel)
                        }
                    }
                }

                apps.sortBy { it.appName }
                flowOf(apps)
            } catch (e: Exception) {
                flowOf(emptyList())
            }
        }
    }

    fun getAppsByCategory(category: String): Flow<List<AppModel>> {
        return flowOf(emptyList())
    }

    suspend fun getAppIcon(packageName: String): Any? {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            appInfo.loadIcon(packageManager)
        } catch (e: Exception) {
            null
        }
    }

    private fun shouldIncludeApp(appInfo: ApplicationInfo, includeSystemApps: Boolean): Boolean {
        val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
        if (isSystem && !includeSystemApps) {
            return false
        }
        // Exclude launcher itself
        if (appInfo.packageName == context.packageName) {
            return false
        }
        return true
    }
}
