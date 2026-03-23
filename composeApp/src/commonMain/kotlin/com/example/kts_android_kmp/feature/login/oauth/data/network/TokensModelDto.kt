package com.example.kts_android_kmp.feature.login.oauth.data.network

data class TokensModelDto(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String,
)