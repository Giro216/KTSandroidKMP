package com.example.kts_android_kmp.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kts_android_kmp.feature.login.models.EmailError
import com.example.kts_android_kmp.feature.login.models.LoginUiEvent
import com.example.kts_android_kmp.feature.login.models.PasswordError
import com.example.kts_android_kmp.utils.PrintCoilImage
import kotlinx.coroutines.flow.collectLatest
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.login_error_message
import ktsandroidkmp.composeapp.generated.resources.logo_content_description
import ktsandroidkmp.composeapp.generated.resources.top_logo_img_url
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = loginViewModel.state.collectAsStateWithLifecycle().value

    // Snackbar для ошибок логина.
    val snackbarHostState = remember { SnackbarHostState() }
    var errorRequestId by remember { mutableIntStateOf(0) }
    val message = stringResource(Res.string.login_error_message)

    LaunchedEffect(errorRequestId) {
        if (errorRequestId > 0) {
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(loginViewModel) {
        loginViewModel.events.collectLatest { event ->
            when (event) {
                is LoginUiEvent.LoginSuccessEvent -> onNavigateToMain()
                is LoginUiEvent.LoginErrorEvent -> {
                    loginViewModel.reset()
                    errorRequestId++
                }
            }
        }
    }

    val emailError: EmailError? = remember(state.username) {
        derivedStateOf { validateEmail(state.username) }
    }.value

    val passwordError: PasswordError? = remember(state.password) {
        derivedStateOf { validatePassword(state.password) }
    }.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LogoHeader()

            LoginForm(
                username = state.username,
                onUsernameChange = loginViewModel::onUsernameChanged,
                emailError = emailError,
                password = state.password,
                onPasswordChange = loginViewModel::onPasswordChanged,
                passwordError = passwordError,
                passwordVisible = state.passwordVisible,
                onPasswordVisibilityToggle = loginViewModel::onPasswordVisibilityToggled,
                canSubmit = state.isLoginButtonActive,
                onSubmit = { loginViewModel.onLoginClicked() },
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
        )
    }
}

@Composable
private fun LogoHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        PrintCoilImage(
            imageUrlRes = Res.string.top_logo_img_url,
            contentDescription = stringResource(Res.string.logo_content_description),
            modifier = Modifier
                .fillMaxWidth(0.8f)
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            loginViewModel = LoginViewModel(),
            onNavigateToMain = {},
        )
    }
}
