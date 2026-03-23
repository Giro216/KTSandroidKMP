package com.example.kts_android_kmp.app

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object Bootstrap : Routes()

    @Serializable
    data object HelloScreen : Routes()

    @Serializable
    data object LoginScreen : Routes()

    @Serializable
    data object MainScreen : Routes()

    @Serializable
    data object ProfileScreen : Routes()
}
