package com.example.kts_android_kmp.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kts_android_kmp.feature.intro.HelloScreen
import com.example.kts_android_kmp.feature.login.LoginScreen
import com.example.kts_android_kmp.feature.login.LoginViewModel
import com.example.kts_android_kmp.feature.main.MainScreen
import com.example.kts_android_kmp.platform.exitApp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HelloScreen
    ) {
        composable<Routes.HelloScreen> {
            HelloScreen(
                onLoginButtonClick = { navController.navigate(Routes.LoginScreen) },
            )
        }

        composable<Routes.LoginScreen> {
            LoginScreen(
                loginViewModel = loginViewModel,
                onNavigateToMain = {
                    navController.navigate(Routes.MainScreen) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<Routes.MainScreen> {
            MainScreen(
                onBackPressed = {
                    exitApp()
                }
            )
        }
    }
}
