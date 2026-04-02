package com.github_explorer.kts_android_kmp.core.data.storage.platform

import android.content.Context
import org.koin.core.context.GlobalContext

actual fun getFilesDir(): String {
    val context: Context = GlobalContext.get().get()
    return context.filesDir.absolutePath
}


