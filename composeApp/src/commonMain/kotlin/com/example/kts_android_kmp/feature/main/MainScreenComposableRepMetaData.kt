package com.example.kts_android_kmp.feature.main

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.kts_android_kmp.feature.main.models.GitHubRepoEntity
import com.example.kts_android_kmp.theme.AppColors.AvatarBackground
import com.example.kts_android_kmp.theme.Dimens.RoundedCornerShapeSize
import com.example.kts_android_kmp.theme.Dimens.ScreenTotalPaddingSmall
import ktsandroidkmp.composeapp.generated.resources.Res
import ktsandroidkmp.composeapp.generated.resources.fork_logo
import ktsandroidkmp.composeapp.generated.resources.star_logo
import org.jetbrains.compose.resources.stringResource

@Composable
fun RepoCard(
    repo: GitHubRepoEntity,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RoundedCornerShapeSize),
        tonalElevation = 1.dp,
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
                PrintMetaData(repo.language, repo.stars, repo.forks, repo.updatedAt)
            }
        }
    }
}

@Composable
private fun PrintMetaData(language: String?, stars: Int, forks: Int, updatedAt: String) {
    val starEmoji = stringResource(Res.string.star_logo)
    val forkEmoji = stringResource(Res.string.fork_logo)



    val likeText = remember(starEmoji, stars) { formatMetric(starEmoji, stars) }
    val commentText = remember(forkEmoji, forks) { formatMetric(forkEmoji, forks) }

    RepoMetaLanguage(language)
    RepoMetaText(text = likeText)
    RepoMetaText(text = commentText)
    RepoMetaText(text = updatedAt)

}

@Composable
private fun RepoMetaLanguage(language: String?) {
    if (language.isNullOrBlank()) return

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(colorForLanguage(language)),
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


@Stable
private fun colorForLanguage(language: String): Color {
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

@Stable
private fun formatMetric(emoji: String, count: Int): String {
    return buildString {
        append(emoji)
        append(formatCount(count))
    }
}

@Stable
private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "${count / 1_000_000}M"
        count >= 1_000 -> "${count / 1_000}K"
        else -> count.toString()
    }
}