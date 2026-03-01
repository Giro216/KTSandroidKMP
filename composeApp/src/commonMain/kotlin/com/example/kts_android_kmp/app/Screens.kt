package com.example.kts_android_kmp.app

sealed class Screen(val route: String) {
    data object Hello : Screen("hello_screen")
    data object Login : Screen("login_screen")
}

