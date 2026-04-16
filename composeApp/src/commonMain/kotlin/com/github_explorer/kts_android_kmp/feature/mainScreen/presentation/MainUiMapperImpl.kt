package com.github_explorer.kts_android_kmp.feature.mainScreen.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.github_explorer.kts_android_kmp.feature.mainScreen.domain.MainUiMapper
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.main_screen_found_count
import ktsandroidkmp.composeapp.generated.resources.main_screen_found_count_of_total
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_advice
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_nothing_found
import org.jetbrains.compose.resources.StringResource

class MainUiMapperImpl : MainUiMapper {
    @Stable
    override fun calculateHint(
        query: String,
        reposSize: Int,
        totalCount: Int,
    ): HintContent? {
        return when {
            query.isBlank() -> HintContent.Resource(Res.string.main_screen_search_advice)
            reposSize == 0 -> HintContent.Resource(Res.string.main_screen_search_nothing_found)
            totalCount > 0 -> HintContent.Resource(
                resource = Res.string.main_screen_found_count_of_total,
                args = listOf(reposSize, totalCount),
            )

            else -> HintContent.Resource(
                resource = Res.string.main_screen_found_count,
                args = listOf(reposSize),
            )
        }
    }

    @Stable
    override fun toSearchQuery(event: MainUiEvent, currentQuery: String): String? {
        val raw = when (event) {
            is MainUiEvent.QueryChanged -> event.query
            MainUiEvent.SearchClicked,
            MainUiEvent.RetryClicked -> currentQuery

            // Эти события не должны запускать initial search.
            MainUiEvent.LoadNextPageRequested,
            MainUiEvent.ReposLoaded,
            MainUiEvent.ErrorLoadingRepos -> return null
        }

        val trimmed = raw.trim()
        return trimmed.takeIf { it.isNotBlank() }
    }

}

@Immutable
sealed interface HintContent {
    data class Resource(
        val resource: StringResource,
        val args: List<Any> = emptyList(),
    ) : HintContent

    data class PlainText(val text: String) : HintContent
}
