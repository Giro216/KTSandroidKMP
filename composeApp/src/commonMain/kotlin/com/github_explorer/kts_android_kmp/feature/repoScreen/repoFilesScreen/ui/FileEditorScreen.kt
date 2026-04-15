package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.presentation.RepoFileEditorMode
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.repo_files_screen_content
import ktsandroidkmp.composeapp.generated.resources.repo_files_screen_edit_file
import ktsandroidkmp.composeapp.generated.resources.repo_files_screen_file_name
import ktsandroidkmp.composeapp.generated.resources.repo_files_screen_new_file
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileEditorScreen(
    mode: RepoFileEditorMode,
    fileName: String,
    content: String,
    isUploading: Boolean,
    onFileNameChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = fileName,
                        onValueChange = onFileNameChanged,
                        singleLine = true,
                        enabled = !isUploading,
                        label = { Text(stringResource(Res.string.repo_files_screen_file_name)) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose, enabled = !isUploading) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onSave, enabled = !isUploading) {
                        Icon(Icons.Filled.Check, contentDescription = null)
                    }
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = if (mode == RepoFileEditorMode.CREATE) stringResource(Res.string.repo_files_screen_new_file)
                else stringResource(Res.string.repo_files_screen_edit_file),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = content,
                onValueChange = onContentChanged,
                enabled = !isUploading,
                label = { Text(stringResource(Res.string.repo_files_screen_content)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
            if (isUploading) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}