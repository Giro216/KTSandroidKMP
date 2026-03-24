package com.example.kts_android_kmp.core.data.network

import com.example.kts_android_kmp.core.data.storage.domain.SessionRepository
import kotlinx.coroutines.flow.Flow

class AuthTokenProvider(
    private val sessionRepository: SessionRepository,
) {
    fun accessTokenFlow(): Flow<String?> = sessionRepository.accessToken()
}

