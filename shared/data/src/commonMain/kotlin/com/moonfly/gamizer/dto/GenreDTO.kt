package com.moonfly.gamizer.dto

import com.moonfly.gamizer.model.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreDTO(
    val id: Int,
    val name: String,
    @SerialName("image_background") val image: String,
)

fun GenreDTO.toGenre(): Genre = Genre(this.name, this.image)