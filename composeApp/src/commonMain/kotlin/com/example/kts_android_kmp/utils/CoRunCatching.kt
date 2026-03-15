package com.example.kts_android_kmp.utils

import kotlinx.coroutines.CancellationException

/**
 * Аналог [runCatching] для suspend-кода, который не перехватывает отмену корутины
 */
suspend inline fun <T> coRunCatching(crossinline block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (t: Throwable) {
        Result.failure(t)
    }
}

