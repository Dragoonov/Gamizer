package com.moonfly.gamizer.model

data class Game(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val description: String,
    val website: String,
    val developers: List<Developer>,
    val platforms: List<Platform>,
    val genres: List<Genre>
)