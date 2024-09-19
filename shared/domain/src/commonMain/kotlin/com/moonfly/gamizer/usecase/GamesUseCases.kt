package com.moonfly.gamizer.usecase

import com.moonfly.gamizer.model.Game
import com.moonfly.gamizer.repository.Response

fun interface GetGamesUseCase : suspend (Int) -> Response<List<Game>>
fun interface GetGameDetailsUseCase : suspend (Int) -> Response<Game>
fun interface IsGameLikedUseCase: suspend (Int) -> Response<Boolean>
fun interface LikeGameUseCase: suspend (Int, Boolean) -> Response<Unit>