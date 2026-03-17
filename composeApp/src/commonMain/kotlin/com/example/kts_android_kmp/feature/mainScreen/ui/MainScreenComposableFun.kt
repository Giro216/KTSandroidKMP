package com.example.kts_android_kmp.feature.mainScreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kts_android_kmp.feature.mainScreen.data.mapper.HintContent
import com.example.kts_android_kmp.theme.Dimens.ScreenHorizontalPaddingMedium
import com.example.kts_android_kmp.theme.Dimens.ScreenVerticalPaddingSmall
import com.example.kts_android_kmp.theme.Dimens.SpacingMedium
import com.example.kts_android_kmp.theme.Dimens.SpacingSmall
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.main_screen_data_loading_error
import ktsandroidkmp.composeapp.generated.resources.main_screen_next_page_loading_error
import ktsandroidkmp.composeapp.generated.resources.main_screen_retry_button
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_button
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_exp
import ktsandroidkmp.composeapp.generated.resources.main_screen_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainHeader(
    query: String,
    isInitialError: Boolean,
    hint: HintContent?,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = ScreenVerticalPaddingSmall,
                start = ScreenHorizontalPaddingMedium,
                end = ScreenHorizontalPaddingMedium,
            ),
    ) {
        Text(
            text = stringResource(Res.string.main_screen_title),
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(Modifier.height(SpacingSmall))

        SearchBar(
            query = query,
            onQueryChanged = onQueryChanged,
            onSearch = onSearch,
        )

        if (isInitialError) {
            LoadingError(
                resource = Res.string.main_screen_data_loading_error,
                onRetry = onRetry,
            )
        }

        Spacer(Modifier.height(SpacingSmall))

        if (hint != null) {
            val hintText = when (hint) {
                is HintContent.Resource -> stringResource(hint.resource)
                is HintContent.PlainText -> hint.text
            }
            
            if (hintText.isNotEmpty()) {
                Text(
                    text = hintText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            singleLine = true,
            label = { Text(stringResource(Res.string.main_screen_search_exp)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        )

        Spacer(Modifier.width(10.dp))

        Button(
            onClick = onSearch,
            modifier = Modifier.height(56.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(stringResource(Res.string.main_screen_search_button))
        }
    }
}

@Composable
fun PaginationLoader(
    isPaginationLoading: Boolean,
    isPaginationError: Boolean,
    onRetry: () -> Unit,
) {
    when {
        isPaginationLoading -> {
            LoadingIndicator()
        }

        isPaginationError -> {
            LoadingError(
                resource = Res.string.main_screen_next_page_loading_error,
                onRetry = onRetry,
            )
        }

        else -> {
            Spacer(modifier = Modifier.height(SpacingMedium))
        }
    }
}

@Composable
private fun LoadingError(resource: StringResource, onRetry: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(resource),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
        )
        TextButton(onClick = onRetry) {
            Text(stringResource(Res.string.main_screen_retry_button))
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun MainHeaderPreview() {
    MainHeader(
        query = "Kotlin",
        isInitialError = false,
        hint = HintContent.PlainText("Найдено: 30 из 100"),
        onQueryChanged = {},
        onSearch = {},
        onRetry = {},
    )
}