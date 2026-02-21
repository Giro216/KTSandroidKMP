package com.example.kts_android_kmp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier,
    onLogin: (email: String, password: String) -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val emailError by remember(email) {
        derivedStateOf {
            when {
                email.isBlank() -> "Введите email"
                !isLikelyEmail(email) -> "Некорректный email"
                else -> null
            }
        }
    }

    val passwordError by remember(password) {
        derivedStateOf {
            when {
                password.isBlank() -> "Введите пароль"
                password.length < 6 -> "Минимум 6 символов"
                else -> null
            }
        }
    }

    val canSubmit by remember(emailError, passwordError) {
        derivedStateOf { emailError == null && passwordError == null }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Вход",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Войдите в аккаунт, чтобы продолжить",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("name@example.com") },
            isError = email.isNotEmpty() && emailError != null,
            supportingText = {
                val msg = if (email.isNotEmpty()) emailError else null
                if (msg != null) Text(msg)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            isError = password.isNotEmpty() && passwordError != null,
            supportingText = {
                val msg = if (password.isNotEmpty()) passwordError else null
                if (msg != null) Text(msg)
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль",
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (canSubmit) onLogin(email.trim(), password)
                },
            ),
        )

        Spacer(Modifier.height(4.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = canSubmit,
            onClick = { onLogin(email.trim(), password) },
        ) {
            Text("Войти")
        }
    }
}

private fun isLikelyEmail(email: String): Boolean {
    val s = email.trim()
    val at = s.indexOf('@')
    if (at <= 0 || at == s.lastIndex) return false
    val dot = s.indexOf('.', startIndex = at + 2)
    return dot in (at + 2)..<s.lastIndex
}

@Composable
@Preview
fun LoginScreenPreview() {
    Scaffold{ innerPadding,  ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ){
            val modifier1 = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp
                )
            val onLogin: (email: String, password: String) -> Unit = { _, _ -> }
            LoginScreen(modifier1, onLogin)
        }
    }

}
