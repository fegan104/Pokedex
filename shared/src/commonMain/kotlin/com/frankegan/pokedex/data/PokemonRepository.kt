package com.frankegan.pokedex.data

import com.frankegan.pokedex.data.local.DatabaseDriverFactory
import com.frankegan.pokedex.data.local.PokemonLocalDataSource
import com.frankegan.pokedex.data.remote.PokemonRemoteDataSource
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

class PokemonRepository(driverFactory: DatabaseDriverFactory): PokemonDataSource {

    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
    }
    private val remote: PokemonDataSource = PokemonRemoteDataSource(httpClient)
    private val local: PokemonDataSource = PokemonLocalDataSource(driverFactory)


    override suspend fun getPokemonPagedResult(page: Int): Result<List<Pokemon>> {
        return local.getPokemonPagedResult(page).recoverCatching {
            val pageResult = remote.getPokemonPagedResult(page).getOrThrow()
            for (pokemon in pageResult) {
                local.savePokemon(pokemon)
            }
            pageResult
        }
    }

    override suspend fun getPokemonSpecies(id: Int): Result<PokemonSpecies> {
        return local.getPokemonSpecies(id).recoverCatching {
            val species = remote.getPokemonSpecies(id).getOrThrow()
            local.savePokemonSpecies(species).getOrThrow()
        }
    }

    override suspend fun savePokemon(pokemon: Pokemon): Result<Pokemon> = local.savePokemon(pokemon)

    override suspend fun savePokemonSpecies(species: PokemonSpecies): Result<PokemonSpecies> = local.savePokemonSpecies(species)
}