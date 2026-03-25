package com.github_explorer.kts_android_kmp.feature.mainScreen.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PullToRefreshContainer(
    isRefreshing: Boolean,
    isAtTop: Boolean,
    isScrollInProgress: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    content()
}

