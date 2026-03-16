package com.example.kts_android_kmp.network

import com.example.kts_android_kmp.storage.domain.ISessionRepository
import kotlinx.coroutines.flow.Flow

class AuthTokenProvider(
    private val sessionRepository: ISessionRepository,
) {
    fun accessTokenFlow(): Flow<String?> = sessionRepository.accessToken()
}

