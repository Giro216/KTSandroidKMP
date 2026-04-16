package com.github_explorer.kts_android_kmp.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.feature.settings.presentation.SettingsViewModel
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.settings_back_content_description
import ktsandroidkmp.composeapp.generated.resources.settings_language
import ktsandroidkmp.composeapp.generated.resources.settings_language_english
import ktsandroidkmp.composeapp.generated.resources.settings_language_russian
import ktsandroidkmp.composeapp.generated.resources.settings_theme
import ktsandroidkmp.composeapp.generated.resources.settings_theme_dark
import ktsandroidkmp.composeapp.generated.resources.settings_theme_light
import ktsandroidkmp.composeapp.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.settings_back_content_description),
                        )
                    }
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(onClick = viewModel::toggleTheme, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${stringResource(Res.string.settings_theme)}: ${
                        if (state.isDarkTheme) {
                            stringResource(Res.string.settings_theme_dark)
                        } else {
                            stringResource(Res.string.settings_theme_light)
                        }
                    }",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.settings_language),
                    style = MaterialTheme.typography.titleMedium,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { viewModel.setLanguage("en") }) {
                        Text(stringResource(Res.string.settings_language_english))
                    }

                    Button(onClick = { viewModel.setLanguage("ru-RU") }) {
                        Text(stringResource(Res.string.settings_language_russian))
                    }
                }
            }
        }
    }
}