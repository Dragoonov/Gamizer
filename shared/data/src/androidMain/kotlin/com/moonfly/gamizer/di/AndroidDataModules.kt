package com.moonfly.gamizer.di

import com.moonfly.gamizer.db.DriverFactory
import com.moonfly.gamizer.db.createDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDataModule = module {
    single { createDatabase(DriverFactory(androidContext())) }
}