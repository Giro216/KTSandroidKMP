package com.github_explorer.kts_android_kmp.feature.repoScreen.presentation

import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase.ObserveFavoritesUseCase
import com.github_explorer.kts_android_kmp.feature.favorites.domain.usecase.ToggleFavoriteUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.useCase.LoadDetailsUseCase
import com.github_explorer.kts_android_kmp.feature.repoScreen.domain.useCase.LoadReadmeUseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepoViewModel(
    private val loadReadmeUseCase: LoadReadmeUseCase,
    private val loadDetailsUseCase: LoadDetailsUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : BaseViewModel<RepoUiEvent, RepoUiState>(RepoUiState()) {

    private var favoriteRepoIds: Set<Long> = emptySet()

    init {
        viewModelScope.launch {
            observeFavoritesUseCase().collect { favorites ->
                favoriteRepoIds = favorites.map { it.id }.toSet()
                val detailsId = state.value.details?.id ?: return@collect
                updateState { copy(isStarredLocally = favoriteRepoIds.contains(detailsId)) }
            }
        }
    }

    fun onEvent(event: RepoUiEvent) {
        when (event) {
            is RepoUiEvent.Init -> {
                updateState { copy(owner = event.owner, repo = event.repo) }
                loadAll(owner = event.owner, repo = event.repo)
            }

            RepoUiEvent.Refresh -> {
                val currentOwner = state.value.owner
                val currentRepo = state.value.repo
                if (currentOwner != null && currentRepo != null) {
                    loadAll(owner = currentOwner, repo = currentRepo)
                }
            }

            RepoUiEvent.ToggleFavorite -> {
                val repo = state.value.details ?: return
                viewModelScope.launch {
                    val isFavorite = toggleFavoriteUseCase(repo)
                    updateState { copy(isStarredLocally = isFavorite) }
                }
            }

            RepoUiEvent.RetryLoadDetails -> {
                val currentOwner = state.value.owner
                val currentRepo = state.value.repo
                if (currentOwner != null && currentRepo != null) {
                    viewModelScope.launch {
                        loadDetails(currentOwner, currentRepo)
                    }
                }
            }

            RepoUiEvent.RetryLoadReadme -> {
                val currentOwner = state.value.owner
                val currentRepo = state.value.repo
                if (currentOwner != null && currentRepo != null) {
                    viewModelScope.launch {
                        loadReadme(currentOwner, currentRepo)
                    }
                }
            }

            RepoUiEvent.ShareRepo -> {
                // TODO: side-effect (share)
            }

            RepoUiEvent.CreateIssue -> {
                // TODO: side-effect (open issue URL)
            }
        }
    }

    private fun loadAll(owner: String, repo: String) {
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = true,
                    isError = false,
                    errorMessage = null,
                    isDetailsLoading = true,
                    isReadmeLoading = true,
                )
            }

            loadDetails(owner, repo)
            loadReadme(owner, repo)
        }
    }

    private suspend fun loadDetails(owner: String, repo: String) {
        val detailsResult = withContext(Dispatchers.IO) {
            loadDetailsUseCase.loadRepoDetails(owner, repo)
        }

        detailsResult
            .onSuccess { details ->
                updateState {
                    copy(
                        isLoading = false,
                        isDetailsLoading = false,
                        details = details,
                        isStarredLocally = favoriteRepoIds.contains(details.id),
                        isError = false,
                    )
                }
            }
            .onFailure { throwable ->
                updateState {
                    copy(
                        isLoading = false,
                        isDetailsLoading = false,
                        isError = true,
                        errorMessage = throwable.message,
                    )
                }
            }
    }

    private suspend fun loadReadme(owner: String, repo: String) {
        val readmeResult = withContext(Dispatchers.IO) {
            loadReadmeUseCase.loadReadme(owner, repo)
        }

        readmeResult
            .onSuccess { readme ->
                updateState {
                    copy(
                        isReadmeLoading = false,
                        readme = readme,
                    )
                }
            }
            .onFailure { throwable ->
                updateState {
                    copy(
                        isReadmeLoading = false,
                        isError = true,
                        errorMessage = throwable.message,
                    )
                }
                Napier.e("Failed to load README", throwable)
            }
    }
}
