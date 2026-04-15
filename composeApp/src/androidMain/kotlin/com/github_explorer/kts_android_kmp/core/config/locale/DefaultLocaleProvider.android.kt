package com.github_explorer.kts_android_kmp.core.config.locale

import java.util.Locale

actual fun getDefaultLocale(): String {
    return Locale.getDefault().toString()
}