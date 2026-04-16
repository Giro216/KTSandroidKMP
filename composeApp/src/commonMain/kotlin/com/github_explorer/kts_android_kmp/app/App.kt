package com.github_explorer.kts_android_kmp.app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.common.ui.theme.AppTheme
import com.github_explorer.kts_android_kmp.core.config.locale.LocalAppLocale
import com.github_explorer.kts_android_kmp.core.config.locale.getDefaultLocale
import com.github_explorer.kts_android_kmp.feature.settings.presentation.SettingsViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val vm: SettingsViewModel = koinInject()

    val isDarkTheme = vm.getThemeState()
        .collectAsState(false)
        .value

    val languageCode = vm.getLanguageState()
        .collectAsStateWithLifecycle(getDefaultLocale())
        .value


    CompositionLocalProvider(LocalAppLocale provides languageCode) {
        AppTheme(isDarkTheme = isDarkTheme) {
            Scaffold { innerPadding ->
                Surface {
                    AppNavigation(innerPadding)
                }
            }
        }
    }
}

