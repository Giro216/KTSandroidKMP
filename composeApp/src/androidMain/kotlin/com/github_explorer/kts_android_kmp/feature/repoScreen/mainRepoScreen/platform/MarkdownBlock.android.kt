package com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.feature.settings.presentation.SettingsViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun MarkdownBlock(markdown: String, modifier: Modifier) {
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val isDarkTheme = settingsViewModel.getThemeState()
        .collectAsStateWithLifecycle(false)
        .value
    val markdownParser = koinInject<MarkdownParser>()
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context -> TextView(context) },
        update = { textView ->
            markdownParser.render(markdown = markdown, target = textView, isDarkTheme = isDarkTheme)
        },
    )
}