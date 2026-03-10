package com.example.kts_android_kmp.feature.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.kts_android_kmp.feature.login.models.EmailError
import com.example.kts_android_kmp.feature.login.models.PasswordError
import com.example.kts_android_kmp.theme.Dimens
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.email_error_blank
import ktsandroidkmp.composeapp.generated.resources.email_error_invalid
import ktsandroidkmp.composeapp.generated.resources.email_label
import ktsandroidkmp.composeapp.generated.resources.email_placeholder
import ktsandroidkmp.composeapp.generated.resources.hide_password_content_description
import ktsandroidkmp.composeapp.generated.resources.login_screen_title
import ktsandroidkmp.composeapp.generated.resources.login_screen_title_main
import ktsandroidkmp.composeapp.generated.resources.password_error_blank
import ktsandroidkmp.composeapp.generated.resources.password_error_min_length
import ktsandroidkmp.composeapp.generated.resources.password_label
import ktsandroidkmp.composeapp.generated.resources.ru_login
import ktsandroidkmp.composeapp.generated.resources.show_password_content_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginForm(
    username: String,
    onUsernameChange: (String) -> Unit,
    emailError: EmailError?,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: PasswordError?,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    canSubmit: Boolean,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = Dimens.ScreenHorizontalPaddingLarge,
            vertical = Dimens.ScreenVerticalPaddingMedium
        ),
    ) {
        Text(
            text = stringResource(Res.string.login_screen_title_main),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(Res.string.login_screen_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        UsernameTextField(
            value = username,
            onValueChange = onUsernameChange,
            error = emailError,
        )

        PasswordTextField(
            value = password,
            onValueChange = onPasswordChange,
            error = passwordError,
            passwordVisible = passwordVisible,
            onVisibilityToggle = onPasswordVisibilityToggle,
            onDone = { if (canSubmit) onSubmit() },
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight),
            enabled = canSubmit,
            onClick = onSubmit,
        ) {
            Text(
                text = stringResource(Res.string.ru_login),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
private fun UsernameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    error: EmailError?,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.email_label)) },
        placeholder = { Text(stringResource(Res.string.email_placeholder)) },
        isError = value.isNotEmpty() && error != null,
        supportingText = {
            if (value.isNotEmpty() && error != null) {
                Text(error.getText())
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
    error: PasswordError?,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    onDone: () -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.password_label)) },
        isError = value.isNotEmpty() && error != null,
        supportingText = {
            if (value.isNotEmpty() && error != null) {
                Text(error.getText())
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
                        stringResource(Res.string.hide_password_content_description)
                    } else {
                        stringResource(Res.string.show_password_content_description)
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

@Composable
private fun EmailError.getText(): String {
    return when (this) {
        is EmailError.Blank -> stringResource(Res.string.email_error_blank)
        is EmailError.Invalid -> stringResource(Res.string.email_error_invalid)
    }
}

@Composable
private fun PasswordError.getText(): String {
    return when (this) {
        is PasswordError.Blank -> stringResource(Res.string.password_error_blank)
        is PasswordError.MinLength -> stringResource(
            Res.string.password_error_min_length,
            minLength
        )
    }
}