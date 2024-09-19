package com.moonfly.gamizer.repository

class GamesRepository(private val gamesDataSource: GamesDataSource, private val userPreferencesDataSource: UserPreferencesDataSource) {
    suspend fun getGames(page: Int) = gamesDataSource.getGames(page)

    suspend fun getGameDetails(id: Int) = gamesDataSource.getGameDetails(id)

    suspend fun isGameLiked(id: Int) = userPreferencesDataSource.isGameLiked(id)

    suspend fun changeGameLike(id: Int, liked: Boolean) = userPreferencesDataSource.changeGameLike(id, liked)
}