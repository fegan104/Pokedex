package com.frankegan.pokedex.di

import com.frankegan.pokedex.data.PokemonRepository
import com.frankegan.pokedex.data.local.PokemonLocalDataSource
import com.frankegan.pokedex.data.remote.PokemonRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule, platformModule())
}

// called by iOS etc
fun initKoin() = initKoin {}

val commonModule = module {
    single {
        HttpClient {
            install(JsonFeature) {
                val json = Json { ignoreUnknownKeys = true }
                serializer = KotlinxSerializer(json)
            }
        }
    }
    single { PokemonLocalDataSource(get()) }
    single { PokemonRemoteDataSource(get()) }
    single { PokemonRepository(get(), get()) }
}
