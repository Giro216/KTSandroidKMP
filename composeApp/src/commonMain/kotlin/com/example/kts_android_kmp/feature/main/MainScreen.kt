package com.example.kts_android_kmp.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kts_android_kmp.feature.main.models.MainUiEvent
import com.example.kts_android_kmp.theme.Dimens.ScreenHorizontalPaddingSmall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.main_screen_click_back_twice
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel { MainViewModel() },
    onBackPressed: () -> Unit = {},
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
        if (shouldLoadNext &&
            state.canPaginate &&
            !state.isPaginationLoading &&
            !state.isLoading &&
            !state.isPaginationError
        ) {
            mainViewModel.loadNextPage()
        }
    }

    // Double-back-to-exit
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        mainViewModel.events.collectLatest { event: MainUiEvent ->
            when (event) {
                MainUiEvent.ErrorLoadingRepos -> {
                    snackbarHostState.showSnackbar("Не удалось загрузить репозитории")
                }

                MainUiEvent.ReposLoaded -> Unit
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
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item(key = "header") {
                    MainHeader(
                        query = state.query,
                        isLoading = state.isLoading,
                        isInitialError = state.isInitialError,
                        totalCount = state.totalCount,
                        reposSize = state.repos.size,
                        onQueryChanged = mainViewModel::onQueryChanged,
                        onSearch = mainViewModel::onSearch,
                        onRetry = mainViewModel::retry,
                    )
                }

                item(key = "content") {
                    when {
                        state.isLoading -> {
                            LoadingIndicator()
                        }

                        !state.isLoading && state.repos.isEmpty()  -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "Ничего не найдено",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Попробуйте изменить запрос",
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
                    )
                }

                item(key = "pagination_loader") {
                    PaginationLoader(
                        isPaginationLoading = state.isPaginationLoading,
                        isPaginationError = state.isPaginationError,
                        onRetry = mainViewModel::loadNextPage,
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

@Composable
expect fun MainScreenBackHandler(onBack: () -> Unit)

@Preview
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
