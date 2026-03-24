package com.example.kts_android_kmp.feature.profile.domain

interface ProfileRepository {
    suspend fun loadProfile(): Result<UserProfile>
}
