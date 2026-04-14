package com.github_explorer.kts_android_kmp.feature.repoScreen.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MarkdownBlock(
    markdown: String,
    modifier: Modifier = Modifier,
)