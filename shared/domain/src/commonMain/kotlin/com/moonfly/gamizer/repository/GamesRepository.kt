package com.moonfly.gamizer.repository

import com.moonfly.gamizer.model.Game

class GamesRepository(private val gamesDataSource: GamesDataSource, private val userPreferencesDataSource: UserPreferencesDataSource) {
    suspend fun getGames(page: Int): List<Game> = gamesDataSource.getGames(page)

    suspend fun getGameDetails(id: Int): Game = gamesDataSource.getGameDetails(id)

    suspend fun isGameLiked(id: Int) = userPreferencesDataSource.isGameLiked(id)

    suspend fun changeGameLike(id: Int, liked: Boolean) = userPreferencesDataSource.changeGameLike(id, liked)
}