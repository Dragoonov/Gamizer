package com.moonfly.gamizer.di

import com.moonfly.gamizer.repository.GamesRepository
import com.moonfly.gamizer.usecase.GetGameDetailsUseCase
import com.moonfly.gamizer.usecase.GetGamesUseCase
import com.moonfly.gamizer.usecase.IsGameLikedUseCase
import com.moonfly.gamizer.usecase.LikeGameUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GamesRepository(get(), get()) }
    single { GetGamesUseCase(get<GamesRepository>()::getGames) }
    single { GetGameDetailsUseCase(get<GamesRepository>()::getGameDetails) }
    single { IsGameLikedUseCase(get<GamesRepository>()::isGameLiked) }
    single { LikeGameUseCase(get<GamesRepository>()::changeGameLike) }
}