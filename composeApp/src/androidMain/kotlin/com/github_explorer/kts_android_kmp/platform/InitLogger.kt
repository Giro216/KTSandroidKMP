package com.github_explorer.kts_android_kmp.platform

import com.github_explorer.kts_android_kmp.core.config.logging.CrashlyticsAntilog
import io.github.aakira.napier.Napier

actual fun initLogger() {
    Napier.base(
        CrashlyticsAntilog()
    )
}