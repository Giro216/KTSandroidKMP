package com.example.kts_android_kmp.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.kts_android_kmp.storage.platform.DATA_STORE_FILE_NAME
import com.example.kts_android_kmp.storage.platform.getFilesDir
import com.example.kts_android_kmp.utils.coRunCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

internal object DataStoreProvider {
    private const val FILE_NAME = DATA_STORE_FILE_NAME

    val instance: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { "${getFilesDir()}/$FILE_NAME".toPath() },
        )
    }

    suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        coRunCatching {
            instance.edit { prefs -> prefs[key] = value }
        }
    }

    suspend fun <T> remove(key: Preferences.Key<T>) {
        coRunCatching {
            instance.edit { prefs -> prefs.remove(key) }
        }
    }

    fun <T> read(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return instance.data
            .map { settings ->
                settings[key] ?: defaultValue
            }
    }
}

