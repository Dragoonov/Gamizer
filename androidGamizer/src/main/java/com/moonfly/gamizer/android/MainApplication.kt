package com.moonfly.gamizer.android

import android.app.Application
import com.moonfly.gamizer.di.androidDataModule
import com.moonfly.gamizer.di.dataModule
import com.moonfly.gamizer.di.domainModule
import com.moonfly.gamizer.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(androidDataModule + dataModule + domainModule + presentationModule)
        }
    }
}