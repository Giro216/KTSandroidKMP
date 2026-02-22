package com.example.kts_android_kmp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.kts_android_kmp.ui.components.PrintCoilImage
import com.example.kts_android_kmp.ui.theme.Dimens
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.top_logo_img_url

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
        // Логотип
        LogoHeader()

        // Форма входа
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

//  Private Composables
@Composable
private fun LogoHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        PrintCoilImage(
            imageUrlRes = Res.string.top_logo_img_url,
            contentDescription = "Логотип",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String?,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    canSubmit: Boolean,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = Dimens.ScreenHorizontalPadding,
            vertical = Dimens.ScreenVerticalPadding
        ),
    ) {
        // Заголовки
        Text(
            text = "Вход",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Войдите в аккаунт, чтобы продолжить",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        // Поле Email
        EmailTextField(
            value = email,
            onValueChange = onEmailChange,
            error = emailError,
        )

        // Поле Пароль
        PasswordTextField(
            value = password,
            onValueChange = onPasswordChange,
            error = passwordError,
            passwordVisible = passwordVisible,
            onVisibilityToggle = onPasswordVisibilityToggle,
            onDone = { if (canSubmit) onSubmit() },
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        // Кнопка входа
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight),
            enabled = canSubmit,
            onClick = onSubmit,
        ) {
            Text(
                text = "Войти",
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
private fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text("Email") },
        placeholder = { Text("name@example.com") },
        isError = value.isNotEmpty() && error != null,
        supportingText = {
            if (value.isNotEmpty() && error != null) {
                Text(error)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    onDone: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text("Пароль") },
        isError = value.isNotEmpty() && error != null,
        supportingText = {
            if (value.isNotEmpty() && error != null) {
                Text(error)
            }
        },
        singleLine = true,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (passwordVisible) {
                        Icons.Filled.VisibilityOff
                    } else {
                        Icons.Filled.Visibility
                    },
                    contentDescription = if (passwordVisible) {
                        "Скрыть пароль"
                    } else {
                        "Показать пароль"
                    },
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() },
        ),
    )
}


// Validation
private const val MIN_PASSWORD_LENGTH = 6

private fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Введите email"
        !isValidEmailFormat(email) -> "Некорректный email"
        else -> null
    }
}

private fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Введите пароль"
        password.length < MIN_PASSWORD_LENGTH -> "Минимум $MIN_PASSWORD_LENGTH символов"
        else -> null
    }
}

private fun isValidEmailFormat(email: String): Boolean {
    val trimmed = email.trim()
    val atIndex = trimmed.indexOf('@')
    if (atIndex <= 0 || atIndex == trimmed.lastIndex) return false
    val dotIndex = trimmed.indexOf('.', startIndex = atIndex + 2)
    return dotIndex in (atIndex + 2)..<trimmed.lastIndex
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
