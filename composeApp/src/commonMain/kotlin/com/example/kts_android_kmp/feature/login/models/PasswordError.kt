package com.example.kts_android_kmp.feature.login.models

sealed class PasswordError {
    object Blank : PasswordError()
    data class MinLength(val minLength: Int) : PasswordError()
}

