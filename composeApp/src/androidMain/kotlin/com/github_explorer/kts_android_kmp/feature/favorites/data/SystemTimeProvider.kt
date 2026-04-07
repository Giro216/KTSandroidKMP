package com.github_explorer.kts_android_kmp.feature.favorites.data

class SystemTimeProvider : TimeProvider {
    override fun nowMillis(): Long = System.currentTimeMillis()
}

