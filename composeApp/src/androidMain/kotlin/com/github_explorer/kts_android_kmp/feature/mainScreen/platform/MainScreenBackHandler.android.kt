package com.github_explorer.kts_android_kmp.feature.mainScreen.platform

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun MainScreenBackHandler(onBack: () -> Unit) {
    BackHandler(enabled = true, onBack = onBack)
}

