package com.example.kts_android_kmp.feature.login.oauth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.kts_android_kmp.feature.login.oauth.presentation.LoginViewModel
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.login_screen_loading_gif
import ktsandroidkmp.composeapp.generated.resources.login_waiting_subtitle
import ktsandroidkmp.composeapp.generated.resources.login_waiting_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = koinViewModel(),
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = loginViewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onNavigateToMain()
        } else {
            loginViewModel.openLoginPage()
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        if (!state.isLoggedIn) {
            WaitingForLoginPlaceholder()
        }
    }
}

@Composable
private fun WaitingForLoginPlaceholder(
    modifier: Modifier = Modifier,
) {
    val gifUrl = stringResource(Res.string.login_screen_loading_gif)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.login_waiting_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.login_waiting_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(100.dp))

        Image(
            painter = rememberAsyncImagePainter(model = gifUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .scale(1.8f)
                .height(180.dp),
        )

    }
}
