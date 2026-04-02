package com.github_explorer.kts_android_kmp.core.config.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

class CrashlyticsAntilog(
    private val minCrashlyticsLevel: LogLevel = LogLevel.ERROR
) : Antilog() {

    private class SyntheticLogException(message: String) : RuntimeException(message)

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val tagOrDefault = tag ?: "Napier"
        val msg = message ?: ""

        if (priority >= minCrashlyticsLevel) {
            val crashlytics = FirebaseCrashlytics.getInstance()

            crashlytics.log("${priority.name}/$tagOrDefault: $msg")

            val errorForCrashlytics = throwable ?: SyntheticLogException(
                "${priority.name}/$tagOrDefault: $msg"
            )
            crashlytics.recordException(errorForCrashlytics)
        }
    }
}