package com.frankegan.pokedex.di

import com.frankegan.pokedex.data.PokemonRepository
import com.frankegan.pokedex.data.local.PokemonLocalDataSource
import com.frankegan.pokedex.data.remote.PokemonRemoteDataSource
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule, platformModule())
}

@Suppress("unused") // Called from Swift
fun initKoin() = initKoin {}

val commonModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    single { PokemonLocalDataSource(get()) }
    single { PokemonRemoteDataSource(get()) }
    single { PokemonRepository(get(), get()) }
}
