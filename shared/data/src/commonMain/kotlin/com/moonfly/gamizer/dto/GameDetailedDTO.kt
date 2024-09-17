package com.moonfly.gamizer.dto

import com.moonfly.gamizer.model.Game
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDetailedDTO(
    val id: Int,
    val name: String,
    val description: String,
    val website: String,
    @SerialName("background_image") val backgroundImage: String,
    @SerialName("released") val reselaseDate: String,
    @SerialName("metacritic") val metacriticScore: Double,
    val platforms: List<PlatformHolderDTO>,
    val genres: List<GenreDTO>,
    val developers: List<DeveloperDTO>
)

fun GameDetailedDTO.toGame(): Game = Game(
    id = this.id,
    title = this.name,
    description = this.description,
    website = this.website,
    developers = this.developers.map { it.toDeveloper() },
    imageUrl = this.backgroundImage,
    platforms = platforms.map { it.platform.toPlatform() },
    genres = genres.map { it.toGenre() }
)