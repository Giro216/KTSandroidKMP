package com.example.kts_android_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform