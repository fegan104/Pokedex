package com.frankegan.pokedex.data


interface PokemonDataSource {

    suspend fun getPokemonPage(page: Int): List<Pokemon>

    suspend fun getPokemonSpecies(id: Int): PokemonSpecies

    suspend fun savePokemon(pokemon: Pokemon): Pokemon

    suspend fun savePokemonSpecies(species: PokemonSpecies): PokemonSpecies

    companion object {
        const val PAGE_SIZE = 20
    }
}