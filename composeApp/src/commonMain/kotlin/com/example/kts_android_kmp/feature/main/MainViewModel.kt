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

    private fun demoPosts(): List<FeedPost> {
        val authors = listOf(
            "VK Новости" to "сегодня в 10:15",
            "KMP Dev" to "вчера в 21:42",
            "Музыка" to "2 дня назад",
            "Спорт" to "3 дня назад",
            "Кино" to "неделю назад",
            "Наука" to "только что",
        )

        val imageUrls = listOf(
            "https://fastly.picsum.photos/id/334/900/500.jpg?hmac=gjXDOVIDmsserv6lj3SCmwGvx-CDP__hJPNVfxE1C6U",
            "https://i.pinimg.com/736x/47/a3/78/47a378a579df645f7e8ab8c5e999355c.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTATnU07VSrLPcsurOEADj3di0_WUBNHpDP0w&s",
            "https://img.freepik.com/free-photo/close-up-portrait-beautiful-cat_23-2149214419.jpg?semt=ais_hybrid&w=740&q=80",
            "https://media.licdn.com/dms/image/v2/D5612AQHmKGz8yVW8nQ/article-cover_image-shrink_720_1280/B56ZaAJVqtGUAI-/0/1745906655992?e=2147483647&v=beta&t=0MvQexTk4g2AkPfXfy9ZGv2bVaJjAh763d01oUvK9aY",
        )

        val baseTexts = listOf(
            "Обновление ленты: теперь посты выглядят аккуратнее. А ещё можно быстро пролистать вниз.",
            "Compose Multiplatform — это когда один экран и на Android, и на iOS. Магия? Нет — практика.",
            "Подборка для работы: лоу‑фай, эмбиент и немного синта.",
            "Новая функция: лайк по двойному тапу. Проверяем анимации и состояния.",
            "Если видишь это сообщение — скролл и key в LazyColumn работают стабильно.",
        )

        val result = mutableListOf<FeedPost>()

        // посты для крайних случаев верстки.
        result += FeedPost(
            id = "p_0_long_text",
            authorName = "Тест Верстки",
            authorSubtitle = "edge case",
            text = buildString {
                append("Очень длинный текст: ")
                repeat(12) {
                    append("Compose делает рекомпозиции, когда меняется state. ")
                }
                append("\n\nПроверка переносов строк и ограничений по высоте.")
            },
            likes = 0,
            comments = 0,
            reposts = 0,
            views = 1,
            imageUrl = null,
        )
        result += FeedPost(
            id = "p_1_no_image",
            authorName = "Пост без картинки",
            authorSubtitle = "edge case",
            text = "Картинки нет (imageUrl=null). UI должен выглядеть нормально.",
            likes = 12,
            comments = 3,
            reposts = 0,
            views = 120,
            imageUrl = null,
        )

        // Основная масса моковых данных для проверки производительности и рекомпозиций.
        val count = 20
        for (i in 0 until count) {
            val (author, subtitle) = authors[i % authors.size]

            val imageUrl = when {
                i % 5 == 0 -> null
                else -> imageUrls[i % imageUrls.size]
            }

            val text = when {
                i % 11 == 0 -> baseTexts[0] + "\n\n" + baseTexts[4]
                i % 7 == 0 -> baseTexts[1]
                i % 3 == 0 -> baseTexts[2]
                else -> baseTexts[(i + 3) % baseTexts.size]
            }

            result += FeedPost(
                id = "p_${i + 2}",
                authorName = author,
                authorSubtitle = subtitle,
                text = text,
                likes = (i * 7) % 1500,
                comments = (i * 3) % 250,
                reposts = (i * 2) % 90,
                views = 1_000 + (i * 357),
                imageUrl = imageUrl,
            )
        }

        return result
    }
}
