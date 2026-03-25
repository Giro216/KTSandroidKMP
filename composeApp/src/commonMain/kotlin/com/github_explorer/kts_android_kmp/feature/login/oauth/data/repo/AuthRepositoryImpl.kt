package com.github_explorer.kts_android_kmp.feature.login.oauth.data.repo

import com.github_explorer.kts_android_kmp.core.data.storage.domain.SessionRepository
import com.github_explorer.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto
import com.github_explorer.kts_android_kmp.feature.login.oauth.domain.AuthRepository

class AuthRepositoryImpl(
    private val sessionRepository: SessionRepository,
) : AuthRepository {

    override suspend fun saveTokens(tokens: TokensModelDto): Result<Unit> =
        sessionRepository.saveTokens(tokens)

    override suspend fun logout(): Result<Unit> =
        sessionRepository.clearTokens()
}