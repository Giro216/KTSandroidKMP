package com.github_explorer.kts_android_kmp.core.config.logging

import com.github_explorer.kts_android_kmp.BuildConfig
import io.github.aakira.napier.Napier

fun initLogger() {
    if (BuildConfig.LOGGING_ENABLED) {
        Napier.base(ConsoleAntilog())
    } else {
        Napier.base(CrashlyticsAntilog())
    }
}