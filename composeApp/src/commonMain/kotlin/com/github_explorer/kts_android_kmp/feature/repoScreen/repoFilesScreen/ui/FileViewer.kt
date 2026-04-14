package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.ui//package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github_explorer.kts_android_kmp.feature.repoScreen.mainRepoScreen.platform.MarkdownBlock
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.FileType
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileContent
import kotlin.io.encoding.Base64

@Composable
fun FileViewer(
    file: RepoFileContent
) {
    val fileType = remember(file.name) {
        getFileType(file.name)
    }

    when (fileType) {
        FileType.MARKDOWN -> MarkdownViewer(file)
        FileType.IMAGE -> ImageViewer(file)
        FileType.CODE,
        FileType.JSON,
        FileType.TEXT -> TextViewer(file)

        else -> UnknownViewer(file)
    }
}

@Composable
fun MarkdownViewer(file: RepoFileContent) {
    val markdown = remember(file.content) {
        decodeBase64(file.content)
    }

    MarkdownBlock(markdown = markdown)
}

@Composable
fun ImageViewer(file: RepoFileContent) {
    val url = file.downloadUrl ?: return

    AsyncImage(
        model = url,
        contentDescription = file.name,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TextViewer(file: RepoFileContent) {
    val text = remember(file.content) {
        decodeBase64(file.content)
    }

    SelectionContainer {
        Text(
            text = text,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun UnknownViewer(file: RepoFileContent) {
    Text("Preview not available for ${file.name}")
}

fun getFileType(name: String): FileType {
    return when {
        name.endsWith(".md", true) -> FileType.MARKDOWN
        name.endsWith(".png", true) ||
                name.endsWith(".jpg", true) ||
                name.endsWith(".jpeg", true) ||
                name.endsWith(".gif", true) -> FileType.IMAGE

        name.endsWith(".kt", true) ||
                name.endsWith(".kts", true) ||
                name.endsWith(".java", true) ||
                name.endsWith(".pro", true) ||
                name.endsWith(".gitignore", true) ||
                name.endsWith(".xml", true) -> FileType.CODE

        name.endsWith(".json", true) -> FileType.JSON
        name.endsWith(".txt", true) -> FileType.TEXT
        else -> FileType.UNKNOWN
    }
}

fun decodeBase64(content: String?): String {
    if (content == null) return ""

    val cleaned = content.replace("\n", "")
    val bytes = Base64.decode(cleaned).decodeToString()
    return bytes
}