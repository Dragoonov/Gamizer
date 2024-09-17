package com.moonfly.gamizer.dto

import com.moonfly.gamizer.model.Game
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDTO(
    val id: Int,
    val name: String,
    @SerialName("background_image") val backgroundImage: String,
    @SerialName("released") val reselaseDate: String,
    @SerialName("metacritic") val metacriticScore: Double,
    val platforms: List<PlatformHolderDTO>,
    val genres: List<GenreDTO>
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