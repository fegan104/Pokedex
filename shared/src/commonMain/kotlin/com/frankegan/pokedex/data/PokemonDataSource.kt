package com.frankegan.pokedex.data

const val PAGE_SIZE = 20


interface PokemonDataSource {

    suspend fun getPokemonPagedResult(page: Int): Result<List<Pokemon>>

    suspend fun getPokemonSpecies(id: Int): Result<PokemonSpecies>

    suspend fun savePokemon(pokemon: Pokemon): Result<Pokemon>

    suspend fun savePokemonSpecies(species: PokemonSpecies): Result<PokemonSpecies>
}