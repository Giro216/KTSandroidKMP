package com.example.kts_android_kmp.feature.mainScreen.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.example.kts_android_kmp.theme.AppColors.AvatarBackground
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_advice
import ktsandroidkmp.composeapp.generated.resources.main_screen_search_nothing_found
import org.jetbrains.compose.resources.StringResource

interface IMainUiMapper {
    fun calculateHint(
        query: String,
        reposSize: Int,
        totalCount: Int,
    ): HintContent?

    fun toSearchQuery(event: MainUiEvent, currentQuery: String): String?

    fun formatCount(count: Int): String

    fun formatMetric(emoji: String, count: Int): String

    fun colorForLanguage(language: String): Color
}

class MainUiMapper : IMainUiMapper {
    @Stable
    override fun calculateHint(
        query: String,
        reposSize: Int,
        totalCount: Int,
    ): HintContent? {
        return when {
            query.isBlank() -> HintContent.Resource(Res.string.main_screen_search_advice)
            reposSize == 0 -> HintContent.Resource(Res.string.main_screen_search_nothing_found)
            totalCount > 0 -> HintContent.PlainText("Найдено: $reposSize из $totalCount")
            else -> HintContent.PlainText("Найдено: $reposSize")
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

    @Stable
    override fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000 -> "${count / 1_000_000}M"
            count >= 1_000 -> "${count / 1_000}K"
            else -> count.toString()
        }
    }

    @Stable
    override fun formatMetric(emoji: String, count: Int): String {
        return buildString {
            append(emoji)
            append( formatCount(count))
        }
    }

    @Stable
    override fun colorForLanguage(language: String): Color {
        return when (language.lowercase()) {
            "kotlin" -> Color(0xFFA97BFF)
            "java" -> Color(0xFFB07219)
            "swift" -> Color(0xFFFFAC45)
            "javascript" -> Color(0xFFF1E05A)
            "typescript" -> Color(0xFF3178C6)
            "c" -> Color(0xFF555555)
            "c++" -> Color(0xFFF34B7D)
            "python" -> Color(0xFF3572A5)
            else -> AvatarBackground
        }
    }
}

@Immutable
sealed interface HintContent {
    data class Resource(val resource: StringResource) : HintContent
    data class PlainText(val text: String) : HintContent
}