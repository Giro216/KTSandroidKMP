package com.example.kts_android_kmp.feature.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun MainScreenBackHandler(onBack: () -> Unit) {
    BackHandler(enabled = true, onBack = onBack)
}

