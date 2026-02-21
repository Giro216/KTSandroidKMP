package com.example.kts_android_kmp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kts_android_kmp.ui.navigation.Screen
import io.github.aakira.napier.Napier

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Hello.route
    ) {
        composable(Screen.Hello.route) {
            HelloScreen(
                onLoginButtonClick = { navController.navigate(Screen.Login.route) },
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { email, password ->
                    run {
                        Napier.w("email: $email, password: $password")
                    }
                }
            )
        }
    }
}
