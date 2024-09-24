package com.moonfly.gamizer.dto

import com.moonfly.gamizer.model.Game
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDTO(
    val id: Int,
    val name: String = "",
    @SerialName("background_image") val backgroundImage: String = "",
    @SerialName("released") val reselaseDate: String = "",
    @SerialName("metacritic") val metacriticScore: Double = 0.0,
    val platforms: List<PlatformHolderDTO> = emptyList(),
    val genres: List<GenreDTO> = emptyList()
)

fun GameDTO.toGame(): Game = Game(
    id = id,
    title = this.name,
    description = "",
    website = "",
    developers = emptyList(),
    imageUrl = this.backgroundImage,
    platforms = platforms.map { it.platform.toPlatform() },
    genres = genres.map { it.toGenre() }
)