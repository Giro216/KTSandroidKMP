package com.example.kts_android_kmp.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Универсальный компонент для загрузки изображения по URL из ресурсов.
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
    val painter = rememberAsyncImagePainter(imageUrl)
    val state by painter.state.collectAsState()

    PrintCoilImageProcess(painter, state, contentDescription, modifier)
}

@Composable
fun PrintCoilImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val painter = rememberAsyncImagePainter(imageUrl)
    val state by painter.state.collectAsState()

    PrintCoilImageProcess(painter, state, contentDescription, modifier)
}

@Composable
fun PrintCoilImageProcess(
    painter: AsyncImagePainter,
    state: AsyncImagePainter.State,
    contentDescription: String?,
    modifier: Modifier
) {
    when (state) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
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