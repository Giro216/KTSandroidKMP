package com.example.kts_android_kmp.storage.platform

import android.content.Context

lateinit var appContext: Context
    private set

fun initContext(context: Context) {
    appContext = context.applicationContext
}

actual fun getFilesDir(): String = appContext.filesDir.absolutePath


