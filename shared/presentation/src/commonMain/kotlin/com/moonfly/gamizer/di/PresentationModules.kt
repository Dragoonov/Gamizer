package com.moonfly.gamizer.di

import com.moonfly.gamizer.gamedetails.GameDetailsViewModel
import com.moonfly.gamizer.gamelist.GameListViewModel
import com.moonfly.gamizer.preferences.PreferencesViewModel
import com.moonfly.gamizer.base.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::GameListViewModel)
    viewModelOf(::GameDetailsViewModel)
    viewModelOf(::PreferencesViewModel)
    viewModelOf(::MainViewModel)
}
