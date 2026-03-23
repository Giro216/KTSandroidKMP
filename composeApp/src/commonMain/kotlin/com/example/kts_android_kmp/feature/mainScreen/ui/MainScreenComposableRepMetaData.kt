package com.example.kts_android_kmp.feature.mainScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kts_android_kmp.feature.mainScreen.domain.GitHubRepo
import com.example.kts_android_kmp.theme.Dimens.RoundedCornerShapeSize
import com.example.kts_android_kmp.theme.Dimens.ScreenTotalPaddingSmall
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.fork_logo
import ktsandroidkmp.composeapp.generated.resources.star_logo
import org.jetbrains.compose.resources.stringResource

@Composable
fun RepoCard(
    repo: GitHubRepo,
    modifier: Modifier = Modifier,
    onFormatMetric: (emoji: String, count: Int) -> String,
    onColorMapping: (language: String) -> Color,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RoundedCornerShapeSize),
    ) {
        Column(modifier = Modifier.padding(ScreenTotalPaddingSmall)) {
            Text(
                text = "${repo.owner} / ${repo.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (!repo.description.isNullOrBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                PrintMetaData(
                    repo.language,
                    repo.stars,
                    repo.forks,
                    repo.updatedAt,
                    onFormatMetric,
                    onColorMapping,
                )
            }
        }
    }
}

@Composable
private fun PrintMetaData(
    language: String?,
    stars: Int,
    forks: Int,
    updatedAt: String,
    onFormatMetric: (emoji: String, count: Int) -> String,
    onColorMapping: (language: String) -> Color,
) {
    val starEmoji = stringResource(Res.string.star_logo)
    val forkEmoji = stringResource(Res.string.fork_logo)


    val likeText = remember(starEmoji, stars) { onFormatMetric(starEmoji, stars) }
    val commentText = remember(forkEmoji, forks) { onFormatMetric(forkEmoji, forks) }

    RepoMetaLanguage(language, onColorMapping)
    RepoMetaText(text = likeText)
    RepoMetaText(text = commentText)
    RepoMetaText(text = updatedAt)

}

@Composable
private fun RepoMetaLanguage(language: String?, onColorMapping: (language: String) -> Color) {
    if (language.isNullOrBlank()) return

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(onColorMapping(language)),
        )
        Spacer(Modifier.width(6.dp))
        RepoMetaText(text = language)
    }
}

@Composable
private fun RepoMetaText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
