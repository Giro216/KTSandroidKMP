package com.github_explorer.kts_android_kmp.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.github_explorer.kts_android_kmp.feature.bootstrap.BootstrapScreen
import com.github_explorer.kts_android_kmp.feature.intro.HelloScreen
import com.github_explorer.kts_android_kmp.feature.login.oauth.ui.LoginScreen
import com.github_explorer.kts_android_kmp.feature.mainScreen.ui.MainBottomTab
import com.github_explorer.kts_android_kmp.feature.mainScreen.ui.MainScreen
import com.github_explorer.kts_android_kmp.feature.repoScreen.issueScreen.ui.IssueScreen
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.ui.RepoScreen
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileItemType
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.ui.RepoFilesScreen
import com.github_explorer.kts_android_kmp.platform.exitApp

@Composable
fun AppNavigation(innerPadding: PaddingValues) {
    val navController = rememberNavController()
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
                        ?.set(forcedTabProperty, MainBottomTab.Repositories.name)
                    navController.navigate(Routes.RepoScreen(owner = owner, repo = repo))
                },
                onOpenFavorites = {
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(forcedTabProperty, MainBottomTab.Favorites.name)
                },
                lazyColumnModifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            )
        }


        composable<Routes.RepoScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.RepoScreen>()
            RepoScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onOpenIssues = { owner, repo ->
                    navController.navigate(Routes.IssueScreen(owner = owner, repo = repo))
                },
                onOpenCode = { owner, repo ->
                    navController.navigate(
                        Routes.RepoFilesScreen(
                            owner = owner,
                            repo = repo,
                            path = "",
                            type = RepoFileItemType.DIR
                        )
                    )
                },
                owner = route.owner,
                repo = route.repo,
            )
        }

        composable<Routes.IssueScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.IssueScreen>()
            IssueScreen(
                owner = route.owner,
                repo = route.repo,
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        composable<Routes.RepoFilesScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.RepoFilesScreen>()
            RepoFilesScreen(
                owner = route.owner,
                repo = route.repo,
                path = route.path,
                contentType = route.type,
                onBackClick = {
                    navController.popBackStack()
                },
                onOpenContent = { newPath, type ->
                    navController.navigate(
                        Routes.RepoFilesScreen(
                            owner = route.owner,
                            repo = route.repo,
                            path = newPath,
                            type = type
                        )
                    )
                },
            )
        }
    }
}
