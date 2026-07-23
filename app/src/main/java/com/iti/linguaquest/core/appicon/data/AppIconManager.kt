package com.iti.linguaquest.core.appicon.dataimport

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.iti.linguaquest.core.appicon.domain.AppIconController
import com.iti.linguaquest.core.appicon.domain.AppIconType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AndroidAppIconController @Inject constructor(
    @ApplicationContext private val context: Context
) : AppIconController {

    private val packageManager: PackageManager = context.packageManager

    override fun switchTo(type: AppIconType) {
        AppIconType.entries.forEach { candidate ->
            updateAlias(candidate, candidate == type)
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