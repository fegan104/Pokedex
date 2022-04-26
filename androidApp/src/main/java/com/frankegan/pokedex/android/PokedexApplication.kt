package com.frankegan.pokedex.android

import android.app.Application
import com.frankegan.pokedex.android.di.appModule
import com.frankegan.pokedex.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.logger.Level

class PokedexApplication: Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(Level.ERROR)
            androidContext(this@PokedexApplication)
            modules(appModule)
        }
    }
}