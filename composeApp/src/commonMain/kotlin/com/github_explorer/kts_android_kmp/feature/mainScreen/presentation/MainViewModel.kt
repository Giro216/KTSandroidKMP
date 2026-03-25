package com.github_explorer.kts_android_kmp.feature.mainScreen.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.github_explorer.kts_android_kmp.common.BaseViewModel
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.MainUiMapper
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.usecase.SearchReposPageUseCase
import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.reducer.MainAction
import com.github_explorer.kts_android_kmp.feature.mainScreen.presentation.reducer.MainReducer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_MS = 1000L
private const val PER_PAGE = 20

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainViewModel(
    private val searchReposPageUseCase: SearchReposPageUseCase,
    private val uiMapper: MainUiMapper,
    private val reducer: MainReducer,
) : BaseViewModel<MainUiEvent, MainUiState>(initialState = MainUiState(), extraBufferCapacity = 1) {

    init {
        viewModelScope.launch {
            events
                .debounce(SEARCH_DEBOUNCE_MS)
                .transformLatest { event ->
                    val query = uiMapper.toSearchQuery(event, state.value.query)
                        ?: return@transformLatest

                    updateState { reducer.reduce(this, MainAction.FirstPageLoading(query)) }

                    val result = searchReposPageUseCase(
                        query = query,
                        page = 1,
                        perPage = PER_PAGE,
                    )

                    emit(query to result)
                }
                .collect { (query, result) ->
                    result
                        .onSuccess { searchResult ->
                            updateState {
                                reducer.reduce(
                                    this,
                                    MainAction.FirstPageSuccess(
                                        query = query,
                                        result = searchResult
                                    ),
                                )
                            }

                            acceptLabel(MainUiEvent.ReposLoaded)
                        }
                        .onFailure { throwable ->
                            updateState {
                                reducer.reduce(
                                    this,
                                    MainAction.FirstPageError(query = query, throwable = throwable),
                                )
                            }
                            acceptLabel(MainUiEvent.ErrorLoadingRepos)
                        }
                }
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
            updateState { reducer.reduce(this, MainAction.NextPageLoading(nextPage)) }

            val result = searchReposPageUseCase(
                query = query,
                page = nextPage,
                perPage = PER_PAGE,
            )

            result
                .onSuccess { searchResult ->
                    updateState {
                        reducer.reduce(
                            this,
                            MainAction.NextPageSuccess(nextPage = nextPage, result = searchResult),
                        )
                    }
                }
                .onFailure { throwable ->
                    updateState { reducer.reduce(this, MainAction.NextPageError(throwable)) }
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
}
