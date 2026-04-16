package com.github_explorer.kts_android_kmp.feature.mainScreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.common.ui.LoadingIndicator
import com.github_explorer.kts_android_kmp.common.ui.theme.AppColors.PrimaryBlue
import com.github_explorer.kts_android_kmp.common.ui.theme.Dimens.ScreenHorizontalPaddingSmall
import com.github_explorer.kts_android_kmp.common.ui.theme.Dimens.headerHeight
import com.github_explorer.kts_android_kmp.common.ui.theme.Strings.LOAD_REPO_ERR
import com.github_explorer.kts_android_kmp.feature.favorites.ui.FavoriteScreen
import com.github_explorer.kts_android_kmp.feature.mainScreen.platform.MainScreenBackHandler
import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.MainUiEvent
import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.MainViewModel
import com.github_explorer.kts_android_kmp.feature.profile.ui.ProfileScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.favorite_title
import ktsandroidkmp.composeapp.generated.resources.hello_screen_title
import ktsandroidkmp.composeapp.generated.resources.main_screen_click_back_twice
import ktsandroidkmp.composeapp.generated.resources.main_screen_retry_search_hint
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_nothing_found
import ktsandroidkmp.composeapp.generated.resources.profile_title
import ktsandroidkmp.composeapp.generated.resources.repos_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

enum class MainBottomTab {
    Repositories,
    Favorites,
    Profile,
}

@Composable
fun MainScreen(
    lazyColumnModifier: Modifier = Modifier,
    mainViewModel: MainViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {},
    onNavigateToBootstrap: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    forcedTab: String? = null,
    onForcedTabConsumed: () -> Unit = {},
    onOpenRepo: (owner: String, repo: String) -> Unit = { _, _ -> },
    onOpenFavorites: () -> Unit = {},
) {
    val state by mainViewModel.state.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableStateOf(MainBottomTab.Repositories) }

    LaunchedEffect(forcedTab) {
        if (forcedTab == MainBottomTab.Favorites.name) {
            selectedTab = MainBottomTab.Favorites
            mainViewModel.loadFavoritesFromStorage()
            onForcedTabConsumed()
        }
    }

    val listState = rememberLazyListState()
    val shouldLoadNext by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount

            total > 0 && lastVisible >= total - 2
        }
    }


    LaunchedEffect(shouldLoadNext) {
        if (shouldLoadNext && mainViewModel.canLoadNextPage()) {
            mainViewModel.loadNextPage()
        }
    }

    // Double-back-to-exit
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        mainViewModel.events.collectLatest { event: MainUiEvent ->
            when (event) {
                MainUiEvent.ErrorLoadingRepos -> {
                    snackbarHostState.showSnackbar(LOAD_REPO_ERR)
                }

                else -> Unit
            }
        }
    }

    var backPressedOnce by remember { mutableStateOf(false) }
    var backHintRequestId by remember { mutableIntStateOf(0) }
    val backHintMessage = stringResource(Res.string.main_screen_click_back_twice)

    LaunchedEffect(backHintRequestId) {
        if (backHintRequestId > 0) snackbarHostState.showSnackbar(backHintMessage)
    }

    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000)
            backPressedOnce = false
        }
    }

    MainScreenBackHandler(
        onBack = {
            if (backPressedOnce) {
                onBackPressed()
            } else {
                backPressedOnce = true
                backHintRequestId++
            }
        }
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == MainBottomTab.Repositories,
                        onClick = { selectedTab = MainBottomTab.Repositories },
                        icon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        label = { Text(stringResource(Res.string.repos_title)) },
                    )
                    NavigationBarItem(
                        selected = selectedTab == MainBottomTab.Favorites,
                        onClick = {
                            selectedTab = MainBottomTab.Favorites
                            mainViewModel.loadFavoritesFromStorage()
                        },
                        icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                        label = { Text(stringResource(Res.string.favorite_title)) },
                    )
                    NavigationBarItem(
                        selected = selectedTab == MainBottomTab.Profile,
                        onClick = { selectedTab = MainBottomTab.Profile },
                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        label = { Text(stringResource(Res.string.profile_title)) },
                    )
                }
            },
        ) { scaffoldPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = scaffoldPadding.calculateBottomPadding()),
            ) {
                when (selectedTab) {
                    MainBottomTab.Repositories -> {
                        PullToRefreshBox(
                            modifier = Modifier.fillMaxSize(),
                            isRefreshing = state.isRefreshing,
                            onRefresh = mainViewModel::refresh,
                        ) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                item(key = "header") {
                                    Row(
                                        modifier = lazyColumnModifier
                                            .fillMaxWidth()
                                            .padding(horizontal = ScreenHorizontalPaddingSmall)
                                            .height(headerHeight),
                                    ) {
                                        Text(
                                            text = stringResource(Res.string.hello_screen_title),
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = PrimaryBlue,
                                        )
                                    }
                                }

                                item(key = "title") {
                                    MainHeader(
                                        query = state.query,
                                        isInitialError = state.isInitialError,
                                        hint = state.hint,
                                        onQueryChanged = mainViewModel::onQueryChanged,
                                        onSearch = mainViewModel::onSearch,
                                        onRetry = mainViewModel::retry,
                                    )
                                }

                                item(key = "content") {
                                    when {
                                        state.isLoading -> LoadingIndicator(24.dp)
                                        !state.isLoading && state.repos.isEmpty() -> {
                                            Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                            ) {
                                                Text(
                                                    text = stringResource(Res.string.main_screen_search_nothing_found),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    textAlign = TextAlign.Center,
                                                )
                                                Spacer(Modifier.height(8.dp))
                                                Text(
                                                    text = stringResource(Res.string.main_screen_retry_search_hint),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    textAlign = TextAlign.Center,
                                                )
                                            }
                                        }
                                    }
                                }

                                items(items = state.repos, key = { it.id }) { repo ->
                                    RepoCard(
                                        repo = repo,
                                        modifier = Modifier.padding(horizontal = ScreenHorizontalPaddingSmall),
                                        onFormatMetric = mainViewModel::formatMetric,
                                        onColorMapping = mainViewModel::colorMapping,
                                        onClick = { onOpenRepo(repo.owner, repo.name) },
                                        isFavorite = state.favoriteRepoIds.contains(repo.id),
                                        onFavoriteClick = { mainViewModel.toggleFavorite(repo) },
                                    )
                                }

                                item(key = "pagination_loader") {
                                    PaginationLoader(
                                        isPaginationLoading = state.pagination.isPaginationLoading,
                                        isPaginationError = state.pagination.isPaginationError,
                                        onRetry = mainViewModel::loadNextPage,
                                    )
                                }
                            }
                        }
                    }

                    MainBottomTab.Favorites -> {
                        onOpenFavorites()
                        FavoriteScreen(
                            repos = state.favoriteRepos,
                            favoriteRepoIds = state.favoriteRepoIds,
                            onOpenRepo = onOpenRepo,
                            onToggleFavorite = mainViewModel::toggleFavorite,
                            onFormatMetric = mainViewModel::formatMetric,
                            onColorMapping = mainViewModel::colorMapping,
                            lazyColumnModifier = lazyColumnModifier,
                        )
                    }

                    MainBottomTab.Profile -> {
                        ProfileScreen(
                            onNavigateToBootstrap = onNavigateToBootstrap,
                            onOpenSettings = onOpenSettings,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
