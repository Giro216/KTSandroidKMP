package com.example.kts_android_kmp.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kts_android_kmp.feature.login.models.EmailError
import com.example.kts_android_kmp.feature.login.models.PasswordError
import com.example.kts_android_kmp.utils.PrintCoilImage
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.logo_content_description
import ktsandroidkmp.composeapp.generated.resources.top_logo_img_url
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLogin: (username: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val state = loginViewModel.state.collectAsStateWithLifecycle().value

    val emailError: EmailError? = remember(state.username) {
        derivedStateOf { validateEmail(state.username) }
    }.value

    val passwordError: PasswordError? = remember(state.password) {
        derivedStateOf { validatePassword(state.password) }
    }.value

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
            onSubmit = { onLogin(state.username.trim(), state.password) },
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
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            loginViewModel = LoginViewModel(),
            onLogin = { _, _ -> },
        )
    }
}
