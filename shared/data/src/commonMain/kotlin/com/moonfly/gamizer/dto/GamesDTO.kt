package com.moonfly.gamizer.dto

import com.moonfly.gamizer.model.Game
import kotlinx.serialization.Serializable

@Serializable
data class GamesDTO(
    val results: List<GameDTO>
)

fun GamesDTO.toGames(): List<Game> = results.map { it.toGame() }