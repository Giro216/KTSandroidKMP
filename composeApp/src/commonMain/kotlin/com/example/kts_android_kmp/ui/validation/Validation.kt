package com.example.kts_android_kmp.ui.validation

import com.example.kts_android_kmp.ui.models.EmailError
import com.example.kts_android_kmp.ui.models.PasswordError

private const val MIN_PASSWORD_LENGTH = 6

fun validateEmail(email: String): EmailError? {
    return when {
        email.isBlank() -> EmailError.Blank
        !isValidEmailFormat(email) -> EmailError.Invalid
        else -> null
    }
}

fun validatePassword(password: String): PasswordError? {
    return when {
        password.isBlank() -> PasswordError.Blank
        password.length < MIN_PASSWORD_LENGTH -> PasswordError.MinLength(MIN_PASSWORD_LENGTH)
        else -> null
    }
}

private fun isValidEmailFormat(email: String): Boolean {
    val trimmed = email.trim()
    val atIndex = trimmed.indexOf('@')
    if (atIndex <= 0 || atIndex == trimmed.lastIndex) return false
    val dotIndex = trimmed.indexOf('.', startIndex = atIndex + 2)
    return dotIndex in (atIndex + 2)..<trimmed.lastIndex
}

