package com.example.kts_android_kmp.feature.main

import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.feature.main.models.GitHubRepository
import com.example.kts_android_kmp.feature.main.models.MainUiEvent
import com.example.kts_android_kmp.feature.main.models.MainUiState
import com.example.kts_android_kmp.network.GitHubApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_MS = 500L

@OptIn(FlowPreview::class)
class MainViewModel(
    private val repo: GitHubRepository = GitHubRepository(GitHubApi()),
) : BaseViewModel<MainUiEvent, MainUiState>(MainUiState()) {

    private val searchRequests = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        // Debounce-поиск по вводу.
        viewModelScope.launch {
            searchRequests
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collect { query ->
                    loadRepos(query)
                }
        }

        loadRepos(query = "kotlin")
    }

    fun onQueryChanged(value: String) {
        updateState { copy(query = value) }
        searchRequests.tryEmit(value.trim())
    }

    fun onSearch() {
        val query = state.value.query.trim()
        if (query.isBlank()) return
        loadRepos(query)
    }

    fun retry() {
        val query = state.value.query.trim().ifBlank { "kotlin" }
        loadRepos(query)
    }

    private fun loadRepos(query: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = false) }

            val result = repo.loadEntities(
                GitHubApi.LoadReposRequestParam(
                    query = query,
                    sort = GitHubApi.LoadReposRequestParam.SortType.STARS,
                    order = "desc",
                    perPage = 20,
                    page = 1,
                )
            )

            result
                .onSuccess { repos ->
                    updateState { copy(repos = repos, isLoading = false, error = false) }
                    acceptLabel(MainUiEvent.ReposLoaded)
                }
                .onFailure {
                    updateState { copy(isLoading = false, error = true) }
                    acceptLabel(MainUiEvent.ErrorLoadingRepos)
                }
        }
    }
}
