package com.example.kts_android_kmp.feature.mainScreen.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.example.kts_android_kmp.common.BaseViewModel
import com.example.kts_android_kmp.feature.mainScreen.data.mapper.HintContent
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubSearchResult
import com.example.kts_android_kmp.feature.mainScreen.domain.IGitHubRepository
import com.example.kts_android_kmp.feature.mainScreen.domain.IMainUiMapper
import com.example.kts_android_kmp.feature.mainScreen.presentation.MainUiState.PaginationUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_MS = 1000L
private const val PER_PAGE = 20

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainViewModel(
    private val repo: IGitHubRepository,
    private val uiMapper: IMainUiMapper,
) : BaseViewModel<MainUiEvent, MainUiState>(initialState = MainUiState(), extraBufferCapacity = 1) {
    
    init {
        viewModelScope.launch {
            events
                .debounce(SEARCH_DEBOUNCE_MS)
                .transformLatest { event ->
                    val query = uiMapper.toSearchQuery(event,state.value.query)
                        ?: return@transformLatest
                    emit(loadFirstPage(query))
                }
                .collect { result -> applyFirstPageResult(result) }
        }

        onQueryChanged("kotlin")
    }

    fun onQueryChanged(value: String) {
        val trimmed = value.trim()
        updateState { copy(query = trimmed) }
        acceptLabel(MainUiEvent.QueryChanged(trimmed))
    }

    fun onSearch() {
        acceptLabel(MainUiEvent.SearchClicked)
    }

    fun retry() {
        val query = state.value.query.trim().ifBlank { "kotlin" }
        updateState { copy(query = query) }
        acceptLabel(MainUiEvent.RetryClicked)
    }

    fun loadNextPage() {
        val current = state.value
        val query = current.query.trim()

        if (!canLoadNextPage()) return
        val nextPage = current.pagination.page + 1

        viewModelScope.launch {
            updateState {
                copy(
                    pagination = pagination.copy(
                        isPaginationLoading = true,
                        isPaginationError = false,
                    )
                )
            }

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
                            pagination = pagination.copy(
                                page = nextPage,
                                isPaginationLoading = false,
                                canPaginate = newRepos.isNotEmpty() && merged.size < total,
                                isPaginationError = false,
                            ),
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            pagination = pagination.copy(
                                isPaginationLoading = false,
                                isPaginationError = true,
                            )
                        )
                    }
                    acceptLabel(MainUiEvent.ErrorLoadingRepos)
                }
        }
    }


    fun canLoadNextPage(): Boolean {
        val current = state.value
        val query = current.query.trim()

        if (query.isBlank()) return false

        return current.pagination.canPaginate &&
                !current.pagination.isPaginationLoading &&
                !current.isLoading &&
                !current.pagination.isPaginationError
    }

    fun formatMetric(emoji: String, count: Int): String = uiMapper.formatMetric(emoji, count)

    fun colorMapping(language: String): Color = uiMapper.colorForLanguage(language)

    private suspend fun loadFirstPage(query: String): Result<GitHubSearchResult> {
        setFirstPageLoading(query)
        return loadPage(query = query, page = 1)
    }

    private fun setFirstPageLoading(query: String) {
        updateState {
            copy(
                query = query,
                isLoading = true,
                isInitialError = false,
                repos = emptyList(),
                totalCount = null,
                hint = HintContent.PlainText(""),
                pagination = PaginationUiState(
                    page = 1,
                    canPaginate = true,
                    isPaginationLoading = false,
                    isPaginationError = false,
                ),
            )
        }
    }

    private fun applyFirstPageResult(result: Result<GitHubSearchResult>) {
        result
            .onSuccess { searchResult ->
                val repos = searchResult.items
                updateState {
                    copy(
                        repos = repos,
                        totalCount = searchResult.totalCount,
                        isLoading = false,
                        isInitialError = false,
                        pagination = pagination.copy(
                            page = 1,
                            isPaginationError = false,
                            canPaginate = repos.isNotEmpty() && repos.size < searchResult.totalCount,
                        ),
                        hint = uiMapper.calculateHint(
                            query = state.value.query,
                            reposSize = repos.size,
                            totalCount = searchResult.totalCount,
                        ),
                    )
                }
            }
            .onFailure {
                updateState { copy(isLoading = false, isInitialError = true, totalCount = null) }
                acceptLabel(MainUiEvent.ErrorLoadingRepos)
            }
    }

    private suspend fun loadPage(query: String, page: Int) = repo.loadEntities(
        repo.initGitHubApiReposRequestParam(
            query = query,
            perPage = PER_PAGE,
            page = page,
        )
    )
}
