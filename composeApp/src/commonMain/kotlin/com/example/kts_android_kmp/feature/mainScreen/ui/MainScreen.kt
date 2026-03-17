package com.example.kts_android_kmp.feature.mainScreen.ui

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kts_android_kmp.feature.mainScreen.platform.MainScreenBackHandler
import com.example.kts_android_kmp.feature.mainScreen.platform.PullToRefreshContainer
import com.example.kts_android_kmp.feature.mainScreen.presentation.MainUiEvent
import com.example.kts_android_kmp.feature.mainScreen.presentation.MainViewModel
import com.example.kts_android_kmp.theme.AppColors.PrimaryBlue
import com.example.kts_android_kmp.theme.Dimens.ScreenHorizontalPaddingSmall
import com.example.kts_android_kmp.theme.Dimens.headerHeight
import com.example.kts_android_kmp.theme.Strings.LOAD_REPO_ERR
import com.example.kts_android_kmp.utils.LoadingIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.hello_screen_title
import ktsandroidkmp.composeapp.generated.resources.main_screen_click_back_twice
import ktsandroidkmp.composeapp.generated.resources.main_screen_retry_search_hint
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_nothing_found
import ktsandroidkmp.composeapp.generated.resources.profile_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
) {
    val state by mainViewModel.state.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val shouldLoadNext by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount

            total > 0 && lastVisible >= total - 2
        }
    }


    LaunchedEffect(shouldLoadNext) {
        if (shouldLoadNext && mainViewModel.canLoadNextPage() ) {
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

    Surface(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            PullToRefreshContainer(
                isRefreshing = false,
                isAtTop = listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0,
                isScrollInProgress = listState.isScrollInProgress,
                onRefresh = mainViewModel::retry,
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    item(key = "header") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = ScreenHorizontalPaddingSmall, vertical = 8.dp)
                                .height(headerHeight),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = stringResource(Res.string.hello_screen_title),
                                style = MaterialTheme.typography.headlineMedium,
                                color = PrimaryBlue,
                            )

                            OutlinedButton(
                                onClick = onOpenProfile,
                            ) {
                                Text(stringResource(Res.string.profile_title))
                            }
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
                            state.isLoading -> {
                                LoadingIndicator(24.dp)
                            }

                            !state.isLoading && state.repos.isEmpty() -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
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

                    items(
                        items = state.repos,
                        key = { it.id },
                    ) { repo ->
                        RepoCard(
                            repo = repo,
                            modifier = Modifier.padding(horizontal = ScreenHorizontalPaddingSmall),
                            onFormatMetric = mainViewModel::formatMetric,
                            onColorMapping = mainViewModel::colorMapping,
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

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
            )
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
