package com.example.gimapp.data.database.preferences

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para acceder a DataStore
val Context.dataStore by preferencesDataStore(name = "data_preferences")

class PreferencesManager(private val context: Context) {

    private val dataStore = context.dataStore

    // Claves para almacenar valores
    companion object {
        val ACTUAL_TRAINING = stringPreferencesKey("training")
        val AGE_KEY = intPreferencesKey("age")
    }

    // Guardar datos
    suspend fun saveData(training: String) {
        dataStore.edit { preferences ->
            preferences[ACTUAL_TRAINING] = training
        }
    }

    // Leer datos
    val trainingData: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ACTUAL_TRAINING]
    }
}
