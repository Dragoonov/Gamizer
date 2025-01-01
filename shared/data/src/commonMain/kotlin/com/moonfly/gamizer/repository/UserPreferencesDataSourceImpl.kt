package com.moonfly.gamizer.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.moonfly.gamizer.GamizerDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesDataSourceImpl(
    private val gamizerDb: GamizerDB,
    private val dataStore: DataStore<Preferences>

) : UserPreferencesDataSource {

    override suspend fun isGameLiked(id: Int): Response<Boolean> {
        return try {
            Response.Success(
                gamizerDb.gamizerDBQueries.select(id.toLong()).executeAsOneOrNull() != null
            )
        } catch (e: IllegalStateException) {
            Response.Error.SerializationError
        }
    }

    override suspend fun changeGameLike(id: Int, liked: Boolean): Response<Unit> {
        if (liked) {
            gamizerDb.gamizerDBQueries.add(id.toLong())
        } else {
            gamizerDb.gamizerDBQueries.delete(id.toLong())
        }
        return Response.Success(Unit)
    }

    override fun getPreferences(): Flow<Response<com.moonfly.gamizer.model.Preferences>> {
        return dataStore.data.map {
            Response.Success(
                com.moonfly.gamizer.model.Preferences(
                    it[PreferencesKeys.DARK_MODE] ?: false
                )
            )
        }
    }

    override suspend fun changeDarkMode(isOn: Boolean): Response<Unit> {
        dataStore.updateData {
            it.toMutablePreferences().apply {
                set(PreferencesKeys.DARK_MODE, isOn)
            }
        }
        return Response.Success(Unit)
    }

    private companion object {
        object PreferencesKeys {
            val DARK_MODE = booleanPreferencesKey("dark_mode")
        }
    }
}