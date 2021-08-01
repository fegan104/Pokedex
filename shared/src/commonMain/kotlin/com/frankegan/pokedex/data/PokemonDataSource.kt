package com.frankegan.pokedex.data

import com.frankegan.pokedex.model.Move
import com.frankegan.pokedex.model.Pokemon
import com.frankegan.pokedex.model.PokemonSpecies


interface PokemonDataSource {

    @Throws(Exception::class)
    suspend fun getPokemonPage(page: Int): List<Pokemon>

    @Throws(Exception::class)
    suspend fun getPokemon(pokemonId: Int): Pokemon

    @Throws(Exception::class)
    suspend fun getPokemonSpecies(id: Int): PokemonSpecies

    @Throws(Exception::class)
    suspend fun savePokemon(pokemon: Pokemon): Pokemon

    @Throws(Exception::class)
    suspend fun savePokemonSpecies(species: PokemonSpecies): PokemonSpecies

    @Throws(Exception::class)
    suspend fun getMoves(pokemonId: Int): List<Move>

    @Throws(Exception::class)
    suspend fun saveMoves(moves: List<Move>): List<Move>

    companion object {
        const val PAGE_SIZE = 20
    }
}