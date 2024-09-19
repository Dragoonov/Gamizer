package com.moonfly.gamizer.di

import com.moonfly.gamizer.gamedetails.GameDetailsViewModel
import com.moonfly.gamizer.gamelist.GameListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::GameListViewModel)
    viewModelOf(::GameDetailsViewModel)
}
