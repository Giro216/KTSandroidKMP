package com.example.kts_android_kmp.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kts_android_kmp.feature.intro.HelloScreen
import com.example.kts_android_kmp.feature.login.LoginScreen
import com.example.kts_android_kmp.feature.login.LoginViewModel
import com.example.kts_android_kmp.feature.main.MainScreen
import com.example.kts_android_kmp.platform.exitApp

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel { LoginViewModel() }

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
                loginViewModel = loginViewModel,
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onBackPressed = {
                    exitApp()
                }
            )
        }
    }
}
