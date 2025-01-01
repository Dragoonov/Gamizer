package com.moonfly.gamizer.usecase

import com.moonfly.gamizer.model.Game
import com.moonfly.gamizer.model.Preferences
import com.moonfly.gamizer.repository.Response
import kotlinx.coroutines.flow.Flow

fun interface GetGamesUseCase : suspend (Int) -> Response<List<Game>>
fun interface GetGameDetailsUseCase : suspend (Int) -> Response<Game>
fun interface IsGameLikedUseCase: suspend (Int) -> Response<Boolean>
fun interface LikeGameUseCase: suspend (Int, Boolean) -> Response<Unit>
fun interface ChangeDarkModeUseCase: suspend (Boolean) -> Response<Unit>
fun interface GetPreferencesFlowUseCase: () -> Flow<Response<Preferences>>