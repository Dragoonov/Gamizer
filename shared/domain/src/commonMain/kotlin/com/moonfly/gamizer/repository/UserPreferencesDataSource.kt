package com.moonfly.gamizer.repository

interface UserPreferencesDataSource {
    suspend fun isGameLiked(id: Int): Response<Boolean>
    suspend fun changeGameLike(id: Int, liked: Boolean): Response<Unit>
}