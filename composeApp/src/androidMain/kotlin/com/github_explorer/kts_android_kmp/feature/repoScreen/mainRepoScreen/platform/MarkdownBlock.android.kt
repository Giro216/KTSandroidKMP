package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.koin.compose.koinInject

@Composable
actual fun MarkdownBlock(markdown: String, modifier: Modifier) {
    val markdownParser = koinInject<MarkdownParser>()
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context -> TextView(context) },
        update = { textView ->
            markdownParser.render(markdown = markdown, target = textView)
        },
    )
}