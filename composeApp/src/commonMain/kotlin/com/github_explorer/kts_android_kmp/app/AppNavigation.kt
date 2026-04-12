package com.github_explorer.kts_android_kmp.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github_explorer.kts_android_kmp.feature.bootstrap.BootstrapScreen
import com.github_explorer.kts_android_kmp.feature.intro.HelloScreen
import com.github_explorer.kts_android_kmp.feature.login.oauth.ui.LoginScreen
import com.github_explorer.kts_android_kmp.feature.mainScreen.ui.MainBottomTab
import com.github_explorer.kts_android_kmp.feature.mainScreen.ui.MainScreen
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.ui.RepoScreen
import com.github_explorer.kts_android_kmp.platform.exitApp

@Composable
fun AppNavigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
    val ownerProperty = "owner"
    val repoProperty = "repo"
    val forcedTabProperty = "forced_tab"

    NavHost(
        navController = navController,
        startDestination = Routes.Bootstrap,
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

        composable<Routes.MainScreen> { backStackEntry ->
            val forcedTab = backStackEntry.savedStateHandle.get<String>(forcedTabProperty)

            MainScreen(
                onBackPressed = {
                    exitApp()
                },
                onNavigateToBootstrap = {
                    navController.navigate(Routes.Bootstrap) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                forcedTab = forcedTab,
                onForcedTabConsumed = {
                    backStackEntry.savedStateHandle[forcedTabProperty] = null
                },
                onOpenRepo = { owner, repo ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(ownerProperty, owner)
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(repoProperty, repo)
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(forcedTabProperty, MainBottomTab.Favorites.name)
                    navController.navigate(Routes.RepoScreen)
                },
                lazyColumnModifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            )
        }


        composable<Routes.RepoScreen> {
            RepoScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                owner = navController.previousBackStackEntry?.savedStateHandle?.get(ownerProperty)
                    ?: "",
                repo = navController.previousBackStackEntry?.savedStateHandle?.get(repoProperty)
                    ?: "",
            )
        }
    }
}
