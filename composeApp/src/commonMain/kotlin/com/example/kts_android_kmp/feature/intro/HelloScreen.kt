package com.example.kts_android_kmp.feature.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.kts_android_kmp.utils.PrintCoilImage
import com.example.kts_android_kmp.theme.AppColors
import com.example.kts_android_kmp.theme.Dimens
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.hello_screen_img_url

@Composable
fun HelloScreen(
    onLoginButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenHorizontalPadding),
        verticalArrangement = Arrangement.Top,
    ) {
        PrintCoilImage(
            imageUrlRes = Res.string.hello_screen_img_url,
            contentDescription = "Приветственное изображение",
            modifier = Modifier
                .fillMaxWidth()
                .scale(Dimens.ImageScale)
                .padding(
                    top = Dimens.HelloImageTopPadding,
                    bottom = Dimens.HelloImageBottomPadding
                )
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

        Text(
            text = "Подберите для себя идеальный автомобиль!",
            style = MaterialTheme.typography.headlineLarge,
            color = AppColors.PrimaryBlue,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.SpacingLarge),
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

        Button(
            onClick = onLoginButtonClick,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = "LOGIN",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview
@Composable
private fun HelloScreenPreview() {
    MaterialTheme {
        HelloScreen(
            onLoginButtonClick = {},
        )
    }
}