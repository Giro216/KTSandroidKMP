package com.example.kts_android_kmp.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kts_android_kmp.feature.bootstrap.BootstrapScreen
import com.example.kts_android_kmp.feature.intro.HelloScreen
import com.example.kts_android_kmp.feature.login.oauth.ui.LoginScreen
import com.example.kts_android_kmp.feature.mainScreen.ui.MainScreen
import com.example.kts_android_kmp.feature.profile.ui.ProfileScreen
import com.example.kts_android_kmp.platform.exitApp

@Composable
fun AppNavigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Bootstrap
    ) {
        composable<Routes.Bootstrap> {
            BootstrapScreen(
                onNavigateToHello = {
                    navController.navigate(Routes.HelloScreen) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Routes.MainScreen) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<Routes.HelloScreen> {
            HelloScreen(
                onLoginButtonClick = {
                    navController.navigate(Routes.LoginScreen)
                },
            )
        }

        composable<Routes.LoginScreen> {
            LoginScreen(
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
                },
                onOpenProfile = {
                    navController.navigate(Routes.ProfileScreen)
                },
                lazyColumnModifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            )
        }

        composable<Routes.ProfileScreen> {
            ProfileScreen(
                onNavigateToBootstrap = {
                    navController.navigate(Routes.Bootstrap) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
