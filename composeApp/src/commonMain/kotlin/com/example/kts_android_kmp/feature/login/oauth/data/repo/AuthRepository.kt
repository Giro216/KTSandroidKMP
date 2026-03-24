package com.example.kts_android_kmp.feature.login.oauth.data.repo

import com.example.kts_android_kmp.core.data.storage.domain.ISessionRepository
import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModelDto
import com.example.kts_android_kmp.feature.login.oauth.domain.IAuthRepository

class AuthRepository(
    private val sessionRepository: ISessionRepository,
) : IAuthRepository {

    override suspend fun saveTokens(tokens: TokensModelDto): Result<Unit> =
        sessionRepository.saveTokens(tokens)

    override suspend fun logout(): Result<Unit> =
        sessionRepository.clearTokens()
}