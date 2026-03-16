package com.example.kts_android_kmp.feature.login.oauth.data.repo

import com.example.kts_android_kmp.feature.login.oauth.data.network.TokensModel
import com.example.kts_android_kmp.feature.login.oauth.domain.IAuthRepository
import com.example.kts_android_kmp.storage.domain.ISessionRepository

class AuthRepository(
    private val sessionRepository: ISessionRepository,
) : IAuthRepository {

    override suspend fun saveTokens(tokens: TokensModel) {
        sessionRepository.saveTokens(tokens)
    }

    override suspend fun logout() {
        sessionRepository.clearTokens()
    }
}