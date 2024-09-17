package com.moonfly.gamizer.repository

import com.moonfly.gamizer.dto.GameDTO
import com.moonfly.gamizer.shared.apiKey
import com.moonfly.gamizer.dto.GamesDTO
import com.moonfly.gamizer.dto.toGame
import com.moonfly.gamizer.dto.toGames
import com.moonfly.gamizer.model.Game
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*


class GamesDataSourceImpl(private val httpClient: HttpClient): GamesDataSource {

    override suspend fun getGames(page: Int): List<Game> {
        return httpClient.get("$API_URL/games") {
                parameter(KEY_KEY, apiKey())
                parameter(PAGE_KEY, page)
                parameter(PAGE_SIZE_KEY, PAGE_SIZE)
            }.body<GamesDTO>().toGames()
    }

    override suspend fun getGameDetails(id: Int): Game {
        return httpClient.get("$API_URL/games/$id") {
                parameter(KEY_KEY, apiKey())
            }.body<GameDTO>().toGame()
    }

    private companion object {
        private const val API_URL = "https://api.rawg.io/api"
        private const val KEY_KEY = "key"
        private const val PAGE_KEY = "page"
        private const val PAGE_SIZE_KEY = "page_size"
        private const val PAGE_SIZE = 10
    }
}