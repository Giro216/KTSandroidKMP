package com.example.kts_android_kmp.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kts_android_kmp.ui.components.LoginForm
import com.example.kts_android_kmp.ui.components.PrintCoilImage
import com.example.kts_android_kmp.ui.validation.validateEmail
import com.example.kts_android_kmp.ui.validation.validatePassword
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.logo_content_description
import ktsandroidkmp.composeapp.generated.resources.top_logo_img_url
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    onLogin: (email: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val emailError by remember(email) {
        derivedStateOf { validateEmail(email) }
    }

    val passwordError by remember(password) {
        derivedStateOf { validatePassword(password) }
    }

    val canSubmit by remember(emailError, passwordError) {
        derivedStateOf { emailError == null && passwordError == null }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        LogoHeader()

        LoginForm(
            email = email,
            onEmailChange = { email = it },
            emailError = emailError,
            password = password,
            onPasswordChange = { password = it },
            passwordError = passwordError,
            passwordVisible = passwordVisible,
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            canSubmit = canSubmit,
            onSubmit = { onLogin(email.trim(), password) },
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
            onLogin = { _, _ -> },
        )
    }
}
