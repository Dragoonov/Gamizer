package com.moonfly.gamizer.repository

import com.moonfly.gamizer.model.Game

interface GamesDataSource {
    suspend fun getGames(page: Int): List<Game>
    suspend fun getGameDetails(id: Int): Game
}