package com.frankegan.pokedex.data

import com.frankegan.pokedex.data.local.PokemonLocalDataSource
import com.frankegan.pokedex.data.remote.PokemonRemoteDataSource
import com.frankegan.pokedex.model.Move
import com.frankegan.pokedex.model.Pokemon
import com.frankegan.pokedex.model.PokemonSpecies
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonRepository : PokemonDataSource, KoinComponent {

    private val remote: PokemonRemoteDataSource by inject()
    private val local: PokemonLocalDataSource by inject()

    override suspend fun getPokemonPage(page: Int): List<Pokemon> {
        return runCatching { local.getPokemonPage(page) }.recoverCatching {
            val pageResult = remote.getPokemonPage(page)
            for (pokemon in pageResult) {
                local.savePokemon(pokemon)
            }
            pageResult
        }.getOrThrow()
    }

    override suspend fun getPokemon(pokemonId: Int): Pokemon {
        return runCatching { local.getPokemon(pokemonId) }.recoverCatching {
            val pokemon = remote.getPokemon(pokemonId)
            local.savePokemon(pokemon)
            pokemon
        }.getOrThrow()
    }

    override suspend fun getPokemonSpecies(id: Int): PokemonSpecies {
        return runCatching { local.getPokemonSpecies(id) }.recoverCatching {
            val species = remote.getPokemonSpecies(id)
            local.savePokemonSpecies(species)
        }.getOrThrow()
    }

    override suspend fun savePokemon(pokemon: Pokemon): Pokemon = local.savePokemon(pokemon)

    override suspend fun savePokemonSpecies(species: PokemonSpecies): PokemonSpecies = local.savePokemonSpecies(species)

    override suspend fun getMoves(pokemonId: Int): List<Move> {
        return runCatching { local.getMoves(pokemonId) }.recoverCatching {
            val moves = remote.getMoves(pokemonId)
            local.saveMoves(moves)
            moves
        }.getOrThrow()
    }

    override suspend fun saveMoves(moves: List<Move>): List<Move> {
        return local.saveMoves(moves)
    }
}