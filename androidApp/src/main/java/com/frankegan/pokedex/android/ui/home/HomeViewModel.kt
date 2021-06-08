package com.frankegan.pokedex.android.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.frankegan.pokedex.android.domain.GetPokemonPageUseCase
import com.frankegan.pokedex.data.Pokemon
import com.frankegan.pokedex.data.PokemonRepository
import com.frankegan.pokedex.data.local.DatabaseDriverFactory
import kotlinx.coroutines.flow.Flow

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val databaseFactory = DatabaseDriverFactory(app)
    private val pokemonRepo = PokemonRepository(databaseFactory)
    private val getPokemonPage = GetPokemonPageUseCase(pokemonRepo)

    fun getPokemon(): Flow<PagingData<Pokemon>> {
        return getPokemonPage().cachedIn(viewModelScope)
    }
}