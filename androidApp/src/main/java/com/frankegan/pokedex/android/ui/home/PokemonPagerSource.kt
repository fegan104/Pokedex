package com.frankegan.pokedex.android.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.frankegan.pokedex.data.Pokemon
import com.frankegan.pokedex.data.PokemonRepository


private const val START_INDEX = 0


class PokemonPagerSource(
    private val pokemonRepo: PokemonRepository
) : PagingSource<Int, Pokemon>() {

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val currentPage = params.key ?: START_INDEX
        val result = pokemonRepo.getPokemonPagedResult(currentPage)
        return result.fold(
            onSuccess = { pokemon ->
                LoadResult.Page(
                    pokemon,
                    prevKey = if (currentPage == START_INDEX) null else currentPage - 1,
                    nextKey = currentPage + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }
}