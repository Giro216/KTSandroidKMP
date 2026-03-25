package com.example.kts_android_kmp.feature.bootstrap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.kts_android_kmp.common.ui.StatusBarSpacer
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BootstrapScreen(
    bootstrapViewModel: BootstrapViewModel = koinViewModel(),
    onNavigateToHello: () -> Unit,
    onNavigateToMain: () -> Unit,
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        StatusBarSpacer()
        CircularProgressIndicator()
    }

    LaunchedEffect(Unit) {
        bootstrapViewModel.events.collectLatest { event ->
            when (event) {
                is BootstrapUiEvent.NavigateToHello -> onNavigateToHello()
                is BootstrapUiEvent.NavigateToMain -> onNavigateToMain()
            }
        }
    }
}

