package com.github_explorer.kts_android_kmp.core.config.logging

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

class CrashlyticsAntilog(
    private val minCrashlyticsLevel: LogLevel = LogLevel.ERROR
) : Antilog() {

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val tagOrDefault = tag ?: "Napier"
        val msg = message ?: ""

        // Logcat всегда логируем
        when (priority) {
            LogLevel.DEBUG -> Log.d(tagOrDefault, msg, throwable)
            LogLevel.INFO -> Log.i(tagOrDefault, msg, throwable)
            LogLevel.WARNING -> Log.w(tagOrDefault, msg, throwable)
            LogLevel.ERROR -> Log.e(tagOrDefault, msg, throwable)
            LogLevel.ASSERT -> Log.wtf(tagOrDefault, msg, throwable)
            LogLevel.VERBOSE -> Log.v(tagOrDefault, msg, throwable)
        }

        // Crashlytics
        if (priority >= minCrashlyticsLevel) {
            val crashlytics = FirebaseCrashlytics.getInstance()

            crashlytics.log("${priority.name}/$tagOrDefault: $msg")

            throwable?.let {
                crashlytics.recordException(it)
            }
        }
    }
}