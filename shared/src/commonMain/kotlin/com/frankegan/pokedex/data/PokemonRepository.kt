package com.frankegan.pokedex.data

import com.frankegan.pokedex.data.local.PokemonLocalDataSource
import com.frankegan.pokedex.data.remote.PokemonRemoteDataSource
import org.koin.core.component.KoinComponent

class PokemonRepository(
    private val remote: PokemonRemoteDataSource,
    private val local: PokemonLocalDataSource
) : PokemonDataSource, KoinComponent {

    override suspend fun getPokemonPagedResult(page: Int): Result<List<Pokemon>> {
        return local.getPokemonPagedResult(page).recoverCatching {
            val pageResult = remote.getPokemonPagedResult(page).getOrThrow()
            for (pokemon in pageResult) {
                local.savePokemon(pokemon)
            }
            pageResult
        }
    }

    override suspend fun getPokemonSpecies(id: Int): Result<PokemonSpecies> {
        return local.getPokemonSpecies(id).recoverCatching {
            val species = remote.getPokemonSpecies(id).getOrThrow()
            local.savePokemonSpecies(species).getOrThrow()
        }
    }

    override suspend fun savePokemon(pokemon: Pokemon): Result<Pokemon> = local.savePokemon(pokemon)

    override suspend fun savePokemonSpecies(species: PokemonSpecies): Result<PokemonSpecies> = local.savePokemonSpecies(species)
}