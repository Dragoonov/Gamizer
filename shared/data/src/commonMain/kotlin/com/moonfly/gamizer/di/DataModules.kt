package com.moonfly.gamizer.di

import com.moonfly.gamizer.repository.GamesDataSourceImpl
import com.moonfly.gamizer.repository.UserPreferencesDataSourceImpl
import com.moonfly.gamizer.repository.GamesDataSource
import com.moonfly.gamizer.repository.UserPreferencesDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single<GamesDataSource> { GamesDataSourceImpl(get()) }
    single<UserPreferencesDataSource> { UserPreferencesDataSourceImpl(get()) }
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
}