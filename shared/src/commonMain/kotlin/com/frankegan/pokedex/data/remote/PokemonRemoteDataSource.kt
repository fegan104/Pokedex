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

    /**
     * TODO is there a more asynchronous way to handle this for Kotlin Native?
     */
    override suspend fun getPokemonPage(page: Int): List<Pokemon> = coroutineScope {
        val pokemonLinks: NamedApiResourceList =
            httpClient.get("$ENDPOINT/pokemon/?limit=$PAGE_SIZE&offset=${page * PAGE_SIZE}")
        val pokemon = pokemonLinks.results.map { result ->
            httpClient.get<Pokemon>(result.url)
        }

        pokemon.sortedBy { it.id }
    }

    override suspend fun getPokemonSpecies(id: Int): PokemonSpecies {
        return httpClient.get("$ENDPOINT/pokemon-species/$id")
    }

    override suspend fun savePokemon(pokemon: Pokemon): Pokemon {
        throw Error("API endpoint is read-only")
    }

    override suspend fun savePokemonSpecies(species: PokemonSpecies): PokemonSpecies {
        throw Error("API endpoint is read-only")
    }
}