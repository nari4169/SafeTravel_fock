package com.example.safetravel.data.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SafeTravelDataSource(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = SHARED_PREFERENCES_NAME
    )

    val authenticationPinFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[PIN_KEY] ?: ""
        }

    suspend fun savePin(pin: String) {
        context.dataStore.edit { preferences -> preferences[PIN_KEY] = pin }
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "safe_travel_shared_preferences"
        private val PIN_KEY = stringPreferencesKey("pin")
    }
}
