package com.example.kts_android_kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.kts_android_kmp.app.App
import com.example.kts_android_kmp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App() }