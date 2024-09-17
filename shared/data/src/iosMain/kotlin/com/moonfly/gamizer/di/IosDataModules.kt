package com.moonfly.gamizer.di

import com.moonfly.gamizer.db.DriverFactory
import com.moonfly.gamizer.db.createDatabase
import org.koin.dsl.module

val iosDataModule = module {
    single { createDatabase(DriverFactory()) }
}