package com.github_explorer.kts_android_kmp.core.config.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue

actual object LocalAppLocale {
    actual val current: String
        @Composable
        get() = "en"

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        TODO("Not yet implemented")
    }

}