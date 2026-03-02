package com.example.kts_android_kmp.feature.main

import androidx.lifecycle.ViewModel
import com.example.kts_android_kmp.feature.main.models.FeedPost
import com.example.kts_android_kmp.feature.main.models.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        MainUiState(
            posts = demoPosts(),
        )
    )
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    private fun demoPosts(): List<FeedPost> = listOf(
        FeedPost(
            id = "p_1",
            authorName = "VK Новости",
            authorSubtitle = "сегодня в 10:15",
            text = "Обновление ленты: теперь посты выглядят аккуратнее. А ещё можно быстро пролистать вниз.",
            likes = 128,
            comments = 24,
            reposts = 3,
            views = 12_310,
            imageUrl = "https://fastly.picsum.photos/id/334/900/500.jpg?hmac=gjXDOVIDmsserv6lj3SCmwGvx-CDP__hJPNVfxE1C6U",
        ),
        FeedPost(
            id = "p_2",
            authorName = "KMP Dev",
            authorSubtitle = "вчера в 21:42",
            text = "Compose Multiplatform — это когда один экран и на Android, и на iOS. Магия? Нет — практика 😄",
            likes = 56,
            comments = 8,
            reposts = 1,
            views = 3_204,
            imageUrl = "https://media.licdn.com/dms/image/v2/D5612AQHmKGz8yVW8nQ/article-cover_image-shrink_720_1280/B56ZaAJVqtGUAI-/0/1745906655992?e=2147483647&v=beta&t=0MvQexTk4g2AkPfXfy9ZGv2bVaJjAh763d01oUvK9aY",
        ),
        FeedPost(
            id = "p_3",
            authorName = "Музыка",
            authorSubtitle = "2 дня назад",
            text = "Подборка для работы: лоу‑фай, эмбиент и немного синта.",
            likes = 1_024,
            comments = 112,
            reposts = 48,
            views = 102_000,
            imageUrl = null,
        ),
    )
}

