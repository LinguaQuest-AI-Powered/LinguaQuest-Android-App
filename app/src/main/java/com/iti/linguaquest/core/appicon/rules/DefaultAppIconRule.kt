package com.iti.linguaquest.core.appicon.rules

import com.iti.linguaquest.core.appicon.domain.AppIconRule
import com.iti.linguaquest.core.appicon.domain.AppIconType
import javax.inject.Inject

class DefaultAppIconRule @Inject constructor() : AppIconRule {
    override val priority: Int = Int.MIN_VALUE

    override suspend fun evaluate(): AppIconType? = AppIconType.DEFAULT
}
