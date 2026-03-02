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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kts_android_kmp.feature.main.models.FeedPost
import com.example.kts_android_kmp.theme.AppColors.AvatarBackground
import com.example.kts_android_kmp.theme.Dimens.PostImageHeight
import com.example.kts_android_kmp.theme.Dimens.RoundedCornerShapeSize
import com.example.kts_android_kmp.theme.Dimens.ScreenHorizontalPaddingMedium
import com.example.kts_android_kmp.theme.Dimens.ScreenHorizontalPaddingSmall
import com.example.kts_android_kmp.theme.Dimens.ScreenTotalPaddingSmall
import com.example.kts_android_kmp.theme.Dimens.ScreenVerticalPaddingSmall
import com.example.kts_android_kmp.theme.Dimens.SpacingMedium
import com.example.kts_android_kmp.theme.Dimens.SpacingSmall
import com.example.kts_android_kmp.utils.PrintCoilImage
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.comment_logo
import ktsandroidkmp.composeapp.generated.resources.like_logo
import ktsandroidkmp.composeapp.generated.resources.main_screen_feed_title
import ktsandroidkmp.composeapp.generated.resources.repost_logo
import ktsandroidkmp.composeapp.generated.resources.views_logo
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

    // Показываем подсказку при первом нажатии Back
    LaunchedEffect(backHintRequestId) {
        if (backHintRequestId > 0) {
            snackbarHostState.showSnackbar("Нажмите назад ещё раз, чтобы выйти")
        }
    }

    // Сбрасываем флаг через 2 секунды
    LaunchedEffect(backPressedOnce) {
        if (backPressedOnce) {
            delay(2000)
            backPressedOnce = false
        }
    }

    // Callback для перехвата Back (вызывается из навигации)
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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item(key = "header") {
                    Text(
                        text = stringResource(Res.string.main_screen_feed_title),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(
                            horizontal = ScreenHorizontalPaddingMedium,
                            vertical = ScreenVerticalPaddingSmall),
                    )
                }

                items(
                    items = state.posts,
                    key = { it.id },
                ) { post ->
                    FeedPostCard(
                        post = post,
                        modifier = Modifier.padding(horizontal = ScreenHorizontalPaddingSmall),
                    )
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(SpacingMedium))
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

/**
 * expect-функция для перехвата Back. Actual реализуется в androidMain.
 */
@Composable
expect fun MainScreenBackHandler(onBack: () -> Unit)

@Composable
private fun FeedPostCard(
    post: FeedPost,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RoundedCornerShapeSize),
        tonalElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(ScreenTotalPaddingSmall)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AvatarPlaceholder(letter = post.authorName.firstOrNull()?.toString() ?: "?")

                Spacer(Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = post.authorSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(Modifier.height(SpacingSmall))

            Text(
                text = post.text,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (post.imageUrl != null) {
                Spacer(Modifier.height(SpacingSmall))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(PostImageHeight)
                        .clip(RoundedCornerShape(RoundedCornerShapeSize))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    PrintCoilImage(post.imageUrl, contentDescription = "Пост изображение", modifier = Modifier.fillMaxSize())
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                val stringBuilder = StringBuilder()
                Metric(text = appendEmoji(
                    builder = stringBuilder,
                    emoji = stringResource(Res.string.like_logo),
                    count = post.likes))

                Metric(text = appendEmoji(
                    builder = stringBuilder,
                    emoji = stringResource(Res.string.comment_logo),
                    count = post.comments))
                Metric(text = appendEmoji(
                    builder = stringBuilder,
                    emoji = stringResource(Res.string.repost_logo),
                    count = post.reposts)
                )
                Metric(text = appendEmoji(
                    builder = stringBuilder,
                    emoji = stringResource(Res.string.views_logo),
                    count = post.views)
                )
            }
        }
    }
}

fun appendEmoji(builder: StringBuilder, emoji: String, count: Int) : String {
    builder.clear()
    builder.append(emoji)
    if (count >= 1000) {
        builder.append("${count / 1000}K")
    } else {
        builder.append(count)
    }
    return builder.toString()
}

@Composable
private fun AvatarPlaceholder(letter: String) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(AvatarBackground),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = letter.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )
    }
}

@Composable
private fun Metric(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview
@Composable
private fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
