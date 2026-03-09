package com.example.kts_android_kmp.feature.main

import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.feature.main.models.GitHubRepositoryImpl
import com.example.kts_android_kmp.feature.main.models.IGitHubRepository
import com.example.kts_android_kmp.feature.main.models.MainUiEvent
import com.example.kts_android_kmp.feature.main.models.MainUiState
import com.example.kts_android_kmp.network.GitHubApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_MS = 1000L
private const val PER_PAGE = 20

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainViewModel(
    private val repo: IGitHubRepository = GitHubRepositoryImpl(GitHubApi()),
) : BaseViewModel<MainUiEvent, MainUiState>(MainUiState()) {

    private val searchRequests = MutableSharedFlow<String>(extraBufferCapacity = 1)
    private val retryRequests = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch {
            merge(
                searchRequests
                    .debounce(SEARCH_DEBOUNCE_MS)
                    .distinctUntilChanged(),
                retryRequests.debounce(SEARCH_DEBOUNCE_MS),
            )
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    flow {
                        emit(loadPage(query = query, page = 1))
                    }
                        .onStart {
                            updateState {
                                copy(
                                    isLoading = true,
                                    isInitialError = false,
                                    isPaginationError = false,
                                    page = 1,
                                    canPaginate = true,
                                    isPaginationLoading = false,
                                    repos = emptyList(),
                                    totalCount = null,
                                )
                            }
                        }
                        .flowOn(Dispatchers.Default)
                }
                .collect { result ->
                    result
                        .onSuccess { searchResult ->
                            val repos = searchResult.items
                            updateState {
                                copy(
                                    repos = repos,
                                    totalCount = searchResult.totalCount,
                                    isLoading = false,
                                    isInitialError = false,
                                    isPaginationError = false,
                                    page = 1,
                                    canPaginate = repos.isNotEmpty() && repos.size < searchResult.totalCount,
                                )
                            }
                            acceptLabel(MainUiEvent.ReposLoaded)
                        }
                        .onFailure {
                            updateState { copy(isLoading = false, isInitialError = true, totalCount = null) }
                            acceptLabel(MainUiEvent.ErrorLoadingRepos)
                        }
                }
        }

        setQuery("kotlin")
    }

    fun onQueryChanged(value: String) {
        setQuery(value)
    }

    fun onSearch() {
        setQuery(state.value.query)
    }

    fun retry() {
        val query = state.value.query.trim().ifBlank { "kotlin" }
        updateState { copy(query = query) }
        retryRequests.tryEmit(query)
    }

    fun loadNextPage() {
        val current = state.value
        val query = current.query.trim()

        if (query.isBlank()) return
        if (!current.canPaginate) return
        if (current.isLoading || current.isPaginationLoading) return
        if (current.isInitialError) return

        val nextPage = current.page + 1

        viewModelScope.launch {
            updateState { copy(isPaginationLoading = true, isPaginationError = false) }

            val result = loadPage(query = query, page = nextPage)

            result
                .onSuccess { searchResult ->
                    val newRepos = searchResult.items
                    updateState {
                        val merged = repos + newRepos
                        val total = totalCount ?: searchResult.totalCount
                        copy(
                            repos = merged,
                            totalCount = total,
                            page = nextPage,
                            isPaginationLoading = false,
                            canPaginate = newRepos.isNotEmpty() && merged.size < total,
                            isPaginationError = false,
                        )
                    }
                }
                .onFailure {
                    updateState { copy(isPaginationLoading = false, isPaginationError = true) }
                    acceptLabel(MainUiEvent.ErrorLoadingRepos)
                }
        }
    }

    private fun setQuery(raw: String) {
        val trimmed = raw.trim()
        updateState { copy(query = trimmed) }
        searchRequests.tryEmit(trimmed)
    }

    private suspend fun loadPage(query: String, page: Int) = repo.loadEntities(
        GitHubApi.LoadReposRequestParam(
            query = query,
            sort = GitHubApi.LoadReposRequestParam.SortType.STARS,
            order = "desc",
            perPage = PER_PAGE,
            page = page,
        )
    )
}
