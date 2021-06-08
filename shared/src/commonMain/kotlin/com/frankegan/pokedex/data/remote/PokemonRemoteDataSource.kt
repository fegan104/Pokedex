package com.frankegan.pokedex.data.remote

import com.frankegan.pokedex.data.NamedApiResourceList
import com.frankegan.pokedex.data.Pokemon
import com.frankegan.pokedex.data.PokemonDataSource
import com.frankegan.pokedex.data.PokemonDataSource.Companion.PAGE_SIZE
import com.frankegan.pokedex.data.PokemonSpecies
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private const val ENDPOINT = "https://pokeapi.co/api/v2"

class PokemonRemoteDataSource(private val httpClient: HttpClient) : PokemonDataSource {

    override suspend fun getPokemonPagedResult(page: Int): Result<List<Pokemon>> = coroutineScope {
        val pokemonLinks: NamedApiResourceList = httpClient.get("$ENDPOINT/pokemon/?limit=$PAGE_SIZE&offset=${page * PAGE_SIZE}")
        val pokemon = pokemonLinks.results.map { result ->
            async { httpClient.get<Pokemon>(result.url) }
        }

        runCatching { pokemon.awaitAll().sortedBy { it.id } }
    }

    override suspend fun getPokemonSpecies(id: Int): Result<PokemonSpecies> {
        return runCatching { httpClient.get("$ENDPOINT/pokemon-species/$id") }
    }

    override suspend fun savePokemon(pokemon: Pokemon): Result<Pokemon> = Result.failure(
        Error("API endpoint is read-only")
    )

    override suspend fun savePokemonSpecies(species: PokemonSpecies): Result<PokemonSpecies> = Result.failure(
        Error("API endpoint is read-only")
    )
}