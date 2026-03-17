package com.example.kts_android_kmp.feature.mainScreen.platform

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@Composable
actual fun PullToRefreshContainer(
    isRefreshing: Boolean,
    isAtTop: Boolean,
    isScrollInProgress: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current

    val latestIsAtTop by rememberUpdatedState(isAtTop)
    val latestIsScrollInProgress by rememberUpdatedState(isScrollInProgress)
    val latestOnRefresh by rememberUpdatedState(onRefresh)

    AndroidView(
        modifier = modifier,
        factory = { _ ->
            val swipeRefresh = SwipeRefreshLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }

            val composeView = ComposeView(context).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent { content() }
            }

            swipeRefresh.addView(composeView)

            swipeRefresh.setOnChildScrollUpCallback { _, _ ->
                !latestIsAtTop
            }

            swipeRefresh.setOnRefreshListener {
                latestOnRefresh()
            }
            swipeRefresh
        },
        update = { swipeRefresh ->
            swipeRefresh.isEnabled = latestIsAtTop && !latestIsScrollInProgress

            if (swipeRefresh.isRefreshing != isRefreshing) {
                swipeRefresh.isRefreshing = isRefreshing
            }
        },
    )
}


