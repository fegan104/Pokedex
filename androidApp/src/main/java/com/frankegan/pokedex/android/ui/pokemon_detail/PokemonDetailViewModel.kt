package com.frankegan.pokedex.android.ui.pokemon_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.frankegan.pokedex.data.PokemonRepository
import com.frankegan.pokedex.model.Move
import com.frankegan.pokedex.model.Pokemon
import com.frankegan.pokedex.model.PokemonSpecies
import kotlinx.coroutines.async

class PokemonDetailViewModel(
    private val pokemonRepo: PokemonRepository
) : ViewModel() {

    fun uiState(pokemonId: Int?): LiveData<UiState> = liveData {
        emit(UiState.Loading)
        if (pokemonId == null) {
            emit(UiState.Error("Missing Pokemon ID"))
            return@liveData
        }
        try {
            val pokemon = pokemonRepo.getPokemon(pokemonId)
            val species = pokemonRepo.getPokemonSpecies(pokemonId)
            emit(UiState.Data(pokemon, species, emptyList()))

            val moves = pokemonRepo.getMoves(pokemonId)
            emit(UiState.Data(pokemon, species, moves))
        } catch (err: Exception) {
            emit(UiState.Error(err.localizedMessage))
        }
    }

    sealed class UiState {
        object Loading : UiState()

        data class Error(val msg: String? = "") : UiState()

        data class Data(
            val pokemon: Pokemon,
            val species: PokemonSpecies,
            val moves: List<Move>
        ): UiState()
    }
}