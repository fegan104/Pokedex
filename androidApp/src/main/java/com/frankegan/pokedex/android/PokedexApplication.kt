package com.frankegan.pokedex.android

import android.app.Application
import com.frankegan.pokedex.android.di.appModule
import com.frankegan.pokedex.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent

class PokedexApplication: Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@PokedexApplication)
            modules(appModule)
        }
    }
}