package com.frankegan.pokedex.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.frankegan.pokedex.android.domain.GetPokemonPageUseCase
import com.frankegan.pokedex.data.Pokemon
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val getPokemonPage: GetPokemonPageUseCase
) : ViewModel() {

    fun getPokemon(): Flow<PagingData<Pokemon>> {
        return getPokemonPage().cachedIn(viewModelScope)
    }
}