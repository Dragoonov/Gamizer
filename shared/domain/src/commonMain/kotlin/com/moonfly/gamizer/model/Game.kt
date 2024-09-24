package com.moonfly.gamizer.model

data class Game(
    val id: Int,
    val title: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val website: String = "",
    val developers: List<Developer> = emptyList(),
    val platforms: List<Platform> = emptyList(),
    val genres: List<Genre> = emptyList()
)