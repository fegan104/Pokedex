package com.frankegan.pokedex.data

import com.frankegan.pokedex.data.local.PokemonLocalDataSource
import com.frankegan.pokedex.data.remote.PokemonRemoteDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PokemonRepository : PokemonDataSource, KoinComponent {

    private val remote: PokemonRemoteDataSource by inject()
    private val local: PokemonLocalDataSource by inject()

    @Throws(Exception::class)
    override suspend fun getPokemonPage(page: Int): List<Pokemon> {
        return runCatching { local.getPokemonPage(page) }.recoverCatching {
            val pageResult = remote.getPokemonPage(page)
            for (pokemon in pageResult) {
                local.savePokemon(pokemon)
            }
            pageResult
        }.getOrThrow()
    }

    @Throws(Exception::class)
    override suspend fun getPokemonSpecies(id: Int): PokemonSpecies {
        return runCatching { local.getPokemonSpecies(id) }.recoverCatching {
            val species = remote.getPokemonSpecies(id)
            local.savePokemonSpecies(species)
        }.getOrThrow()
    }

    @Throws(Exception::class)
    override suspend fun savePokemon(pokemon: Pokemon): Pokemon = local.savePokemon(pokemon)

    @Throws(Exception::class)
    override suspend fun savePokemonSpecies(species: PokemonSpecies): PokemonSpecies =
        local.savePokemonSpecies(species)
}