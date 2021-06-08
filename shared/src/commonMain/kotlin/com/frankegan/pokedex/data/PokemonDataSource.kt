package com.frankegan.pokedex.data


interface PokemonDataSource {

    suspend fun getPokemonPagedResult(page: Int): Result<List<Pokemon>>

    suspend fun getPokemonSpecies(id: Int): Result<PokemonSpecies>

    suspend fun savePokemon(pokemon: Pokemon): Result<Pokemon>

    suspend fun savePokemonSpecies(species: PokemonSpecies): Result<PokemonSpecies>

    companion object {
        const val PAGE_SIZE = 20
    }
}