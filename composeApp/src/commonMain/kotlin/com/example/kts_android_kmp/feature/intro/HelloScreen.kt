package com.example.kts_android_kmp.feature.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kts_android_kmp.theme.AppColors
import com.example.kts_android_kmp.theme.Dimens
import com.example.kts_android_kmp.theme.Dimens.SpacingExtraLarge
import com.example.kts_android_kmp.utils.PrintCoilImage
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.hello_screen_img_url
import ktsandroidkmp.composeapp.generated.resources.hello_screen_subtitle
import ktsandroidkmp.composeapp.generated.resources.hello_screen_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun HelloScreen(
    onLoginButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenHorizontalPaddingLarge),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrintCoilImage(
            imageUrlRes = Res.string.hello_screen_img_url,
            contentDescription = "Приветственное изображение",
            modifier = Modifier
                .scale(1.8f)
                .padding(top = 40.dp)
        )

        Spacer(Modifier.height(SpacingExtraLarge))

        Text(
            text = stringResource(Res.string.hello_screen_title),
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 58.sp,
            color = AppColors.PrimaryBlue,
            modifier = Modifier
                .padding(top = Dimens.SpacingLarge)
        )

        Text(
            text = stringResource(Res.string.hello_screen_subtitle),
            style = MaterialTheme.typography.headlineMedium,
            color = AppColors.PrimaryBlue,
        )

        Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

        Button(
            onClick = {
                onLoginButtonClick()
            },
            shape = MaterialTheme.shapes.large,
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