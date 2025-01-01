package com.moonfly.gamizer.repository

import com.moonfly.gamizer.model.Preferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesDataSource {
    suspend fun isGameLiked(id: Int): Response<Boolean>
    suspend fun changeGameLike(id: Int, liked: Boolean): Response<Unit>
    fun getPreferences(): Flow<Response<Preferences>>
    suspend fun changeDarkMode(isOn: Boolean): Response<Unit>
}