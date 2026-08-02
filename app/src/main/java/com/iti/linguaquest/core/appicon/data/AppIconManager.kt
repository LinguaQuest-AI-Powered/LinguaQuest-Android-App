package com.iti.linguaquest.core.appicon.data

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.iti.linguaquest.core.appicon.domain.AppIconController
import com.iti.linguaquest.core.appicon.domain.AppIconType
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AndroidAppIconController @Inject constructor(
    @ApplicationContext private val context: Context
) : AppIconController {

    private val packageManager: PackageManager = context.packageManager
    private var currentType: AppIconType? = null

    override fun switchTo(type: AppIconType): Boolean {
        Timber.d("AppIcon: Attempting to switch to $type")
        val isAlreadyActive = AppIconType.entries.firstOrNull { candidate ->
            val componentName = ComponentName(
                context.packageName,
                "${context.packageName}.${candidate.aliasActivityName}"
            )
            val state = packageManager.getComponentEnabledSetting(componentName)
            state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||
                    (candidate == AppIconType.DEFAULT && state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
        } == type

        if (isAlreadyActive) {
            Timber.d("AppIcon: Icon is already $type. Skipping.")
            return true
        }

        return try {

            updateAlias(type, enabled = true)
            AppIconType.entries
                .filter { it != type }
                .forEach { candidate -> updateAlias(candidate, enabled = false) }

            Timber.d("AppIcon: Successfully switched to $type")
            currentType = type
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to switch app icon to $type")
            false
        }
    }

    private fun updateAlias(type: AppIconType, enabled: Boolean) {
        val componentName = ComponentName(
            context.packageName,
            "${context.packageName}.${type.aliasActivityName}"
        )

        packageManager.setComponentEnabledSetting(
            componentName,
            if (enabled) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            },
            PackageManager.DONT_KILL_APP
        )
    }
}
