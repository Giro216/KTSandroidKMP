package com.example.kts_android_kmp.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kts_android_kmp.common.ui.PrintCoilImage
import com.example.kts_android_kmp.common.ui.StatusBarSpacer
import com.example.kts_android_kmp.common.ui.theme.Dimens
import com.example.kts_android_kmp.feature.profile.domain.UserProfile
import com.example.kts_android_kmp.feature.profile.presentation.ProfileUiEvent
import com.example.kts_android_kmp.feature.profile.presentation.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.profile_avatar_content_description
import ktsandroidkmp.composeapp.generated.resources.profile_load_error
import ktsandroidkmp.composeapp.generated.resources.profile_logout
import ktsandroidkmp.composeapp.generated.resources.profile_retry
import ktsandroidkmp.composeapp.generated.resources.profile_stat_followers
import ktsandroidkmp.composeapp.generated.resources.profile_stat_repos
import ktsandroidkmp.composeapp.generated.resources.profile_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreen(
    onNavigateToBootstrap: () -> Unit,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    val state by profileViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        profileViewModel.events.collectLatest { event ->
            when (event) {
                ProfileUiEvent.LogoutSuccess -> onNavigateToBootstrap()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.ScreenHorizontalPaddingLarge)
            .padding(top = Dimens.ScreenVerticalPaddingMedium),
        verticalArrangement = Arrangement.Top,
    ) {
        StatusBarSpacer()

        Text(
            text = stringResource(Res.string.profile_title),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Dimens.SpacingLarge))

        when {
            state.isLoading -> {
                LoadingIndicator()
            }

            state.isError -> {
                ProfileLoadingError(profileViewModel::load)
            }

            state.profile != null -> {
                PrintProfile(
                    nullableProfile = state.profile,
                    onLogout = profileViewModel::logout,
                    imageModifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Composable
fun PrintProfile(
    nullableProfile: UserProfile?,
    onLogout: () -> Unit,
    imageModifier: Modifier,
) {
    val profile = requireNotNull(nullableProfile)

    PrintCoilImage(
        imageUrl = profile.avatarUrl,
        contentDescription = stringResource(Res.string.profile_avatar_content_description),
        modifier = imageModifier,
    )

    Spacer(Modifier.height(Dimens.SpacingLarge))

    Text(
        text = profile.name,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
    )

    profile.bio?.takeIf { it.isNotBlank() }?.let { bio ->
        Spacer(Modifier.height(Dimens.SpacingMedium))
        Text(
            text = bio,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }

    Spacer(Modifier.height(Dimens.SpacingMedium))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Stat(
            title = stringResource(Res.string.profile_stat_repos),
            value = profile.publicRepos
        )
        Stat(
            title = stringResource(Res.string.profile_stat_followers),
            value = profile.followers
        )
    }

    Spacer(Modifier.height(Dimens.SpacingLarge))

    Button(
        onClick = onLogout,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(stringResource(Res.string.profile_logout))
    }
}

@Composable
fun ProfileLoadingError(onRetry: () -> Unit) {
    Text(
        text = stringResource(Res.string.profile_load_error),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
    )
    Spacer(Modifier.height(Dimens.SpacingMedium))

    OutlinedButton(onClick = onRetry) {
        Text(stringResource(Res.string.profile_retry))
    }
}

@Composable
private fun Stat(
    title: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = value.toString(), style = MaterialTheme.typography.headlineSmall)
        Text(text = title, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(onNavigateToBootstrap = {})
    }
}

