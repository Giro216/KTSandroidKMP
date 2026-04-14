package com.github_explorer.kts_android_kmp.app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github_explorer.kts_android_kmp.common.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    AppTheme {
        Scaffold { innerPadding ->
            Surface {
                AppNavigation(innerPadding)
            }
        }
    }
}

