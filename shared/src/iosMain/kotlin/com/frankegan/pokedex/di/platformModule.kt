package com.frankegan.pokedex.di

import com.frankegan.pokedex.data.PokemonRepository
import com.frankegan.pokedex.data.local.DatabaseDriverFactory
import org.koin.core.component.KoinComponent
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseDriverFactory() }
}

@Suppress("unused") // Called from Swift
object KotlinDependencies : KoinComponent {
    fun getPokemonRepository() = getKoin().get<PokemonRepository>()
}