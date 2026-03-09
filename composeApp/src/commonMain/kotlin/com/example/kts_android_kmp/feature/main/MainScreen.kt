package com.example.kts_android_kmp.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kts_android_kmp.feature.main.models.GitHubRepoEntity
import com.example.kts_android_kmp.feature.main.models.MainUiEvent
import com.example.kts_android_kmp.theme.AppColors.AvatarBackground
import com.example.kts_android_kmp.theme.Dimens.RoundedCornerShapeSize
import com.example.kts_android_kmp.theme.Dimens.ScreenHorizontalPaddingMedium
import com.example.kts_android_kmp.theme.Dimens.ScreenHorizontalPaddingSmall
import com.example.kts_android_kmp.theme.Dimens.ScreenTotalPaddingSmall
import com.example.kts_android_kmp.theme.Dimens.ScreenVerticalPaddingSmall
import com.example.kts_android_kmp.theme.Dimens.SpacingMedium
import com.example.kts_android_kmp.theme.Dimens.SpacingSmall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.fork_logo
import ktsandroidkmp.composeapp.generated.resources.main_screen_click_back_twice
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_advice
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_button
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_exp
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_nothing_found
import ktsandroidkmp.composeapp.generated.resources.main_screen_title
import ktsandroidkmp.composeapp.generated.resources.star_logo
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel { MainViewModel() },
    onBackPressed: () -> Unit = {},
) {
    val state by mainViewModel.state.collectAsStateWithLifecycle()

    // Double-back-to-exit
    val snackbarHostState = remember { SnackbarHostState() }
    var backPressedOnce by remember { mutableStateOf(false) }
    var backHintRequestId by remember { mutableIntStateOf(0) }
    val backHintMessage = stringResource(Res.string.main_screen_click_back_twice)

    // События экрана (one-shot): ошибки загрузки/успех
    LaunchedEffect(mainViewModel) {
        mainViewModel.events.collectLatest { event: MainUiEvent ->
            when (event) {
                MainUiEvent.ErrorLoadingRepos -> {
                    snackbarHostState.showSnackbar("Не удалось загрузить репозитории")
                }

                MainUiEvent.ReposLoaded -> Unit
            }
        }
    }

    LaunchedEffect(backHintRequestId) {
        if (backHintRequestId > 0) snackbarHostState.showSnackbar(backHintMessage)
    }

    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000)
            backPressedOnce = false
        }
    }

    // Callback для перехвата Back
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
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Ошибка загрузки",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = mainViewModel::retry) {
                            Text("Повторить")
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        item(key = "header") {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = ScreenHorizontalPaddingMedium,
                                        vertical = ScreenVerticalPaddingSmall,
                                    ),
                            ) {
                                Text(
                                    text = stringResource(Res.string.main_screen_title),
                                    style = MaterialTheme.typography.headlineSmall,
                                )

                                Spacer(Modifier.height(SpacingSmall))

                                SearchBar(
                                    query = state.query,
                                    onQueryChanged = mainViewModel::onQueryChanged,
                                    onSearch = mainViewModel::onSearch,
                                )

                                Spacer(Modifier.height(SpacingSmall))

                                val hintText = when {
                                    state.query.isBlank() -> stringResource(Res.string.main_screen_search_advice)
                                    state.repos.isEmpty() -> stringResource(Res.string.main_screen_search_nothing_found)
                                    else -> "Найдено: ${state.repos.size}"
                                }
                                Text(
                                    text = hintText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
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

                        item(key = "bottom_spacer") {
                            Spacer(modifier = Modifier.height(SpacingMedium))
                        }
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

@Composable
expect fun MainScreenBackHandler(onBack: () -> Unit)

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            singleLine = true,
            label = { Text(stringResource(Res.string.main_screen_search_exp)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        )

        Spacer(Modifier.width(10.dp))

        Button(
            onClick = onSearch,
            modifier = Modifier.height(56.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(stringResource(Res.string.main_screen_search_button))
        }
    }
}

@Composable
private fun RepoCard(
    repo: GitHubRepoEntity,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RoundedCornerShapeSize),
        tonalElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(ScreenTotalPaddingSmall)) {
            // owner/name
            Text(
                text = "${repo.owner} / ${repo.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (!repo.description.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                PrintMetaData(repo.language, repo.stars, repo.forks, repo.updatedAt)
            }
        }
    }
}

@Composable
private fun PrintMetaData(language: String?, stars: Int, forks: Int, updatedAt: String) {
    val starEmoji = stringResource(Res.string.star_logo)
    val forkEmoji = stringResource(Res.string.fork_logo)



    val likeText = remember(starEmoji, stars) { formatMetric(starEmoji, stars) }
    val commentText = remember(forkEmoji, forks) { formatMetric(forkEmoji, forks) }

    RepoMetaLanguage(language)
    RepoMetaText(text = likeText)
    RepoMetaText(text = commentText)
    RepoMetaText(text = updatedAt)

}

@Composable
private fun RepoMetaLanguage(language: String?) {
    if (language.isNullOrBlank()) return

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(colorForLanguage(language)),
        )
        Spacer(Modifier.width(6.dp))
        RepoMetaText(text = language)
    }
}

@Composable
private fun RepoMetaText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}


@Stable
private fun colorForLanguage(language: String): Color {
    return when (language.lowercase()) {
        "kotlin" -> Color(0xFFA97BFF)
        "java" -> Color(0xFFB07219)
        "swift" -> Color(0xFFFFAC45)
        "javascript" -> Color(0xFFF1E05A)
        "typescript" -> Color(0xFF3178C6)
        "c" -> Color(0xFF555555)
        "c++" -> Color(0xFFF34B7D)
        "python" -> Color(0xFF3572A5)
        else -> AvatarBackground
    }
}

@Stable
private fun formatMetric(emoji: String, count: Int): String {
    return buildString {
        append(emoji)
        append(formatCount(count))
    }
}

@Stable
private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "${count / 1_000_000}M"
        count >= 1_000 -> "${count / 1_000}K"
        else -> count.toString()
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
