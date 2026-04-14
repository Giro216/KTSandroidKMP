package com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.data.repo

import com.github_explorer.kts_android_kmp.core.data.network.GitHubApi
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.data.network.CreateOrUpdateFileRequestDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.data.network.RepoDirContentDto
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoDirItem
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFileItemType
import com.github_explorer.kts_android_kmp.feature.repoScreen.repoFilesScreen.domain.RepoFilesRepository
import com.github_explorer.kts_android_kmp.utils.coRunCatching
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class RepoFilesRepositoryImpl(
    private val api: GitHubApi,
    private val json: Json,
) : RepoFilesRepository {

    override suspend fun listContents(
        owner: String,
        repo: String,
        path: String
    ): Result<List<RepoDirItem>> {
        return coRunCatching {
            val response = api.getRepoContentsRaw(owner = owner, repo = repo, path = path)
            val raw = response.body<String>()
            parseContentsResponse(raw)
                .sortedWith(compareBy<RepoDirItem> {
                    it.type != RepoFileItemType.DIR
                }.thenBy {
                    it.name.lowercase()
                })
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun createFile(
        owner: String,
        repo: String,
        path: String,
        contentUtf8: String,
        message: String,
    ): Result<Unit> {
        return coRunCatching {
            val encoded = Base64.encode(contentUtf8.encodeToByteArray())
            api.createOrUpdateFile(
                owner = owner,
                repo = repo,
                path = path,
                request = CreateOrUpdateFileRequestDto(
                    message = message,
                    content = encoded,
                    sha = null,
                ),
            )
            Unit
        }.mapFailureToDomainMessage()
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun updateFile(
        owner: String,
        repo: String,
        path: String,
        contentUtf8: String,
        message: String,
    ): Result<Unit> {
        return coRunCatching {
            val currentSha = loadFileSha(owner = owner, repo = repo, path = path)
                ?: throw IllegalStateException("Cannot resolve sha for file: $path")

            val encoded = Base64.encode(contentUtf8.encodeToByteArray())
            api.createOrUpdateFile(
                owner = owner,
                repo = repo,
                path = path,
                request = CreateOrUpdateFileRequestDto(
                    message = message,
                    content = encoded,
                    sha = currentSha,
                ),
            )
            Unit
        }.mapFailureToDomainMessage()
    }

    private suspend fun loadFileSha(owner: String, repo: String, path: String): String? {
        val response = api.getRepoContentsRaw(owner = owner, repo = repo, path = path)
        val raw = response.body<String>()
        val element = json.parseToJsonElement(raw)
        val asObject = element as? JsonObject ?: return null
        return asObject["sha"]?.jsonPrimitive?.contentOrNull
    }

    private suspend fun parseContentsResponse(raw: String): List<RepoDirItem> {
        return when (val element = json.parseToJsonElement(raw)) {
            // при запросе вернулся массив элементов (директория)
            is JsonArray -> element.jsonArray.mapNotNull { decodeContentItem(it.jsonObject) }

            // при запросе вернулся один элемент (файл)
            is JsonObject -> listOfNotNull(decodeContentItem(element.jsonObject))
            else -> emptyList()
        }
    }

    private suspend fun decodeContentItem(obj: JsonObject): RepoDirItem? {
        val dto = coRunCatching {
            // TODO добавить парсинг для чтения содержимого файла
            json.decodeFromJsonElement(RepoDirContentDto.serializer(), obj)
        }.getOrNull() ?: return null

        val type = when (dto.type.lowercase()) {
            "dir" -> RepoFileItemType.DIR
            "file" -> RepoFileItemType.FILE
            else -> return null
        }

        return RepoDirItem(
            name = dto.name,
            path = dto.path,
            type = type,
            sha = dto.sha,
            size = dto.size,
        )
    }
}

private fun <T> Result<T>.mapFailureToDomainMessage(): Result<T> {
    return this.exceptionOrNull()?.let { throwable ->
        val mapped = when (throwable) {
            is ClientRequestException -> {
                when (throwable.response.status) {
                    HttpStatusCode.Conflict -> IllegalStateException(
                        "Конфликт: файл изменился на сервере (sha mismatch). Обновите данные и попробуйте снова.",
                        throwable
                    )

                    HttpStatusCode.UnprocessableEntity -> IllegalStateException(
                        "Ошибка валидации (422). Возможно, файл уже существует или путь/имя некорректны.",
                        throwable
                    )

                    HttpStatusCode.Unauthorized -> IllegalStateException(
                        "Не авторизован (401).",
                        throwable
                    )

                    HttpStatusCode.Forbidden -> IllegalStateException(
                        "Нет доступа (403). Проверьте права/скоупы токена.",
                        throwable
                    )

                    HttpStatusCode.NotFound -> IllegalStateException(
                        "Ресурс не найден (404).",
                        throwable
                    )

                    else -> throwable
                }
            }

            is ServerResponseException -> IllegalStateException(
                "Ошибка сервера GitHub: ${throwable.response.status}",
                throwable
            )

            else -> throwable
        }
        Result.failure(mapped)
    } ?: this
}