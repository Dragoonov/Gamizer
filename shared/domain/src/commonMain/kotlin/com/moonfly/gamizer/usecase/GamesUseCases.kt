package com.moonfly.gamizer.usecase

import com.moonfly.gamizer.model.Game

fun interface GetGamesUseCase : suspend (Int) -> List<Game>
fun interface GetGameDetailsUseCase : suspend (Int) -> Game
fun interface IsGameLikedUseCase: suspend (Int) -> Boolean
fun interface LikeGameUseCase: suspend (Int, Boolean) -> Boolean