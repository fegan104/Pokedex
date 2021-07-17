package com.frankegan.pokedex.data


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

    companion object {
        const val PAGE_SIZE = 20
    }
}