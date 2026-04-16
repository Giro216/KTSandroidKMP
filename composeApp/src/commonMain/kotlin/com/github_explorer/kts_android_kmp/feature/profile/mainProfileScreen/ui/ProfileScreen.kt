package com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github_explorer.kts_android_kmp.common.ui.PrintCoilImage
import com.github_explorer.kts_android_kmp.common.ui.StatusBarSpacer
import com.github_explorer.kts_android_kmp.common.ui.theme.AppColors
import com.github_explorer.kts_android_kmp.common.ui.theme.Dimens
import com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.presentation.ProfileUiState
import com.github_explorer.kts_android_kmp.feature.profile.mainProfileScreen.presentation.ProfileViewModel
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.private_repos_title
import ktsandroidkmp.composeapp.generated.resources.profile_avatar_content_description
import ktsandroidkmp.composeapp.generated.resources.profile_load_error
import ktsandroidkmp.composeapp.generated.resources.profile_retry
import ktsandroidkmp.composeapp.generated.resources.profile_settings_content_description
import ktsandroidkmp.composeapp.generated.resources.profile_stat_followers
import ktsandroidkmp.composeapp.generated.resources.profile_title
import ktsandroidkmp.composeapp.generated.resources.public_repos_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onOpenSettings: () -> Unit,
    onOpenUserRepos: () -> Unit,
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    val state by profileViewModel.state.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        profileViewModel.events.collectLatest { event ->
//            when (event) {
//
//            }
//        }
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Dimens.ScreenHorizontalPaddingLarge)
            .padding(top = Dimens.ScreenVerticalPaddingMedium),
        verticalArrangement = Arrangement.Top,
    ) {
        StatusBarSpacer()

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = stringResource(Res.string.profile_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.Center),
            )

            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(Res.string.profile_settings_content_description),
                )
            }
        }

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
                    profileState = state,
                    onOpenUserRepos = onOpenUserRepos,
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
    profileState: ProfileUiState,
    onOpenUserRepos: () -> Unit,
    imageModifier: Modifier,
) {
    val profile = requireNotNull(profileState.profile)

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

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Stat(
                title = stringResource(Res.string.public_repos_title),
                value = profile.publicRepos,
            )
            Stat(
                title = stringResource(Res.string.profile_stat_followers),
                value = profile.followers
            )
        }

        PrivateRepsButton(
            state = profileState,
            onOpenUserRepos = onOpenUserRepos,
            modifier = Modifier.padding(top = Dimens.SpacingLarge)
        )
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PrivateRepsButton(state: ProfileUiState, onOpenUserRepos: () -> Unit, modifier: Modifier) {
    OutlinedButton(
        onClick = onOpenUserRepos,
        enabled = (state.profile?.privateRepos ?: 0) > 0,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(Res.string.private_repos_title),
                style = MaterialTheme.typography.bodyLargeEmphasized,
                color = AppColors.PrimaryBlue,
            )

            Text(
                text = "${state.profile?.privateRepos ?: 0}",
                style = MaterialTheme.typography.bodyLargeEmphasized,
                color = AppColors.PrimaryBlue,
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(onOpenSettings = {}, onOpenUserRepos = {})
    }
}

