package com.frankegan.pokedex.android.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.frankegan.pokedex.model.Pokemon
import com.frankegan.pokedex.data.PokemonDataSource
import com.frankegan.pokedex.data.PokemonRepository
import kotlinx.coroutines.flow.Flow

class GetPokemonPageUseCase(
    private val pokemonRepo: PokemonRepository
) {

    operator fun invoke(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = PokemonDataSource.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokemonPagerSource(pokemonRepo) }
        ).flow
    }
}