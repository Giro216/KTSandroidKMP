package com.example.kts_android_kmp.common.ui

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Универсальный компонент для загрузки изображения по URL.
 *
 * Отображает:
 * - Индикатор загрузки во время загрузки
 * - Сообщение об ошибке при неудаче
 * - Само изображение при успехе
 *
 * @param imageUrlRes Строковый ресурс с URL изображения
 * @param contentDescription Описание для accessibility
 * @param modifier Модификатор для Image
 */
@Composable
fun PrintCoilImage(
    imageUrlRes: StringResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val imageUrl = stringResource(imageUrlRes)
    PrintCoilImage(
        imageUrl = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}

@Composable
fun PrintCoilImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val painter = rememberAsyncImagePainter(model = imageUrl)

    val stableContentDescription = remember(contentDescription) { contentDescription }
    val stableModifier = remember(modifier) { modifier }

    PrintCoilImageProcess(
        painter = painter,
        contentDescription = stableContentDescription,
        modifier = stableModifier,
    )
}

@Composable
fun PrintCoilImageProcess(
    painter: AsyncImagePainter,
    contentDescription: String?,
    modifier: Modifier,
) {
    val state = painter.state.collectAsStateWithLifecycle()

    when (state.value) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
            LoadingIndicator()
        }

        is AsyncImagePainter.State.Error -> {
            Text(
                text = "Не удалось загрузить картинку",
                color = MaterialTheme.colorScheme.error
            )
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier
            )
        }
    }
}