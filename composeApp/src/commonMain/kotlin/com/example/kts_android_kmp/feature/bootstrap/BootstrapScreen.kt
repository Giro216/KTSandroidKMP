package com.example.kts_android_kmp.feature.bootstrap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kts_android_kmp.app.Routes
import io.github.aakira.napier.Napier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BootstrapScreen(
    vm: BootstrapViewModel = koinViewModel(),
    onNavigate: (Any) -> Unit,
) {
    val state = vm.state.collectAsStateWithLifecycle().value

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

    LaunchedEffect(state.isLoading, state.onboardingShown, state.isLoggedIn) {
        if (!state.isLoading) {
            val destination = when {
                !state.onboardingShown && !state.isLoggedIn -> Routes.HelloScreen
                else -> Routes.MainScreen
            }
            Napier.i("Bootstrap: navigating to $destination", tag = "BootstrapScreen")
            Napier.i("Bootstrap state: isLoading=${state.isLoading}, onboardingShown=${state.onboardingShown}, isLoggedIn=${state.isLoggedIn}", tag = "BootstrapScreen")
            onNavigate(destination)
        }
    }
}

