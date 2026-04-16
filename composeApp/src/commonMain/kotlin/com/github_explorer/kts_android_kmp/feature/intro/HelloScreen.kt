package com.github_explorer.kts_android_kmp.feature.intro

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github_explorer.kts_android_kmp.common.ui.PrintCoilImage
import com.github_explorer.kts_android_kmp.common.ui.theme.AppColors
import com.github_explorer.kts_android_kmp.common.ui.theme.Dimens
import com.github_explorer.kts_android_kmp.common.ui.theme.Dimens.SpacingMedium
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.hello_screen_img_url
import ktsandroidkmp.composeapp.generated.resources.hello_screen_profile_description
import ktsandroidkmp.composeapp.generated.resources.hello_screen_profile_img_url
import ktsandroidkmp.composeapp.generated.resources.hello_screen_profile_title
import ktsandroidkmp.composeapp.generated.resources.hello_screen_repositories_description
import ktsandroidkmp.composeapp.generated.resources.hello_screen_repositories_img_url
import ktsandroidkmp.composeapp.generated.resources.hello_screen_repositories_title
import ktsandroidkmp.composeapp.generated.resources.hello_screen_subtitle
import ktsandroidkmp.composeapp.generated.resources.hello_screen_title
import ktsandroidkmp.composeapp.generated.resources.login
import org.jetbrains.compose.resources.stringResource

private data class IntroSlide(
    val titleRes: org.jetbrains.compose.resources.StringResource,
    val descriptionRes: org.jetbrains.compose.resources.StringResource,
    val imageUrlRes: org.jetbrains.compose.resources.StringResource,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HelloScreen(
    onLoginButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val slides = remember {
        listOf(
            IntroSlide(
                titleRes = Res.string.hello_screen_title,
                descriptionRes = Res.string.hello_screen_subtitle,
                imageUrlRes = Res.string.hello_screen_img_url,
            ),
            IntroSlide(
                titleRes = Res.string.hello_screen_repositories_title,
                descriptionRes = Res.string.hello_screen_repositories_description,
                imageUrlRes = Res.string.hello_screen_repositories_img_url,
            ),
            IntroSlide(
                titleRes = Res.string.hello_screen_profile_title,
                descriptionRes = Res.string.hello_screen_profile_description,
                imageUrlRes = Res.string.hello_screen_profile_img_url,
            ),
        )
    }

    val pagerState = rememberPagerState(pageCount = { slides.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenHorizontalPaddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 360.dp),
            ) { page ->
                IntroSlideCard(slide = slides[page])
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(slides.size) { index ->
                    val selected = index == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .width(if (selected) 22.dp else 8.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selected) AppColors.PrimaryBlue else AppColors.PrimaryBlue.copy(
                                    alpha = 0.28f
                                )
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(SpacingMedium))

        Button(
            onClick = onLoginButtonClick,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.padding(bottom = Dimens.SpacingLarge),
        ) {
            Text(
                text = stringResource(Res.string.login),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
private fun IntroSlideCard(slide: IntroSlide) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            PrintCoilImage(
                imageUrlRes = slide.imageUrlRes,
                contentDescription = stringResource(slide.titleRes),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.55f)
                    .clip(RoundedCornerShape(24.dp)),
            )
            Text(
                text = stringResource(slide.titleRes),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.PrimaryBlue,
                textAlign = TextAlign.Center,
            )

            Text(
                text = stringResource(slide.descriptionRes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
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
