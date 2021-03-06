package com.frankegan.pokedex.data.remote

import com.frankegan.pokedex.data.PokemonDataSource
import com.frankegan.pokedex.data.PokemonDataSource.Companion.PAGE_SIZE
import com.frankegan.pokedex.model.Move
import com.frankegan.pokedex.model.NamedApiResourceList
import com.frankegan.pokedex.model.Pokemon
import com.frankegan.pokedex.model.PokemonSpecies
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

private const val ENDPOINT = "https://pokeapi.co/api/v2"

class PokemonRemoteDataSource(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : PokemonDataSource {

    override suspend fun getPokemonPage(page: Int): List<Pokemon> = withContext(dispatcher) {
        val pokemonLinks: NamedApiResourceList = httpClient.get(
            "$ENDPOINT/pokemon/?limit=$PAGE_SIZE&offset=${page * PAGE_SIZE}"
        ).body()

        val pokemon: List<Pokemon> = pokemonLinks.results.mapAsync { result ->
            httpClient.get(result.url).body()
        }

        pokemon.sortedBy { it.id }
    }

    override suspend fun getPokemon(pokemonId: Int): Pokemon = withContext(dispatcher) {
        httpClient.get("$ENDPOINT/pokemon/$pokemonId").body()
    }

    override suspend fun getPokemonSpecies(id: Int): PokemonSpecies = withContext(dispatcher) {
        httpClient.get("$ENDPOINT/pokemon-species/$id").body()
    }

    override suspend fun savePokemon(pokemon: Pokemon): Pokemon {
        throw Error("API endpoint is read-only")
    }

    override suspend fun savePokemonSpecies(species: PokemonSpecies): PokemonSpecies {
        throw Error("API endpoint is read-only")
    }

    override suspend fun getMoves(pokemonId: Int): List<Move> = withContext(dispatcher) {
        val pokemon = getPokemon(pokemonId)
        pokemon.moves.mapAsync { httpClient.get(it.move.url).body() }
    }

    override suspend fun getMoves(pokemonId: Int, moveIds: List<Int>): List<Move> {
        return moveIds.mapAsync { moveId ->
            httpClient.get("$ENDPOINT/move/$moveId").body()
        }
    }

    override suspend fun saveMoves(moves: List<Move>): List<Move> {
        throw Error("API endpoint is read-only")
    }
}

private suspend fun <A, B> Iterable<A>.mapAsync(f: suspend (A) -> B): List<B> =
    coroutineScope { map { async { f(it) } }.awaitAll() }