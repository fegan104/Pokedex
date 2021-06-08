package com.frankegan.pokedex.di

import com.frankegan.pokedex.data.local.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
}