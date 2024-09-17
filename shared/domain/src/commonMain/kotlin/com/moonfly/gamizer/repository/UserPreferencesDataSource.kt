package com.moonfly.gamizer.repository

interface UserPreferencesDataSource {
    suspend fun isGameLiked(id: Int): Boolean
    suspend fun changeGameLike(id: Int, liked: Boolean): Boolean
}