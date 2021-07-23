package com.frankegan.pokedex.android.ui.pokemon_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.frankegan.pokedex.data.PokemonRepository
import java.lang.Exception

class PokemonDetailViewModel(
    private val pokemonRepo: PokemonRepository
): ViewModel() {

    fun pokemonAndSpecies(pokemonId: Int?) = liveData {
        if (pokemonId == null) {
            emit(null)
            return@liveData
        }
        try {
            val pokemon = pokemonRepo.getPokemon(pokemonId)
            val species = pokemonRepo.getPokemonSpecies(pokemonId)
            Log.d("PokemonDetailViewModel", pokemon.stats.toString())
            emit(pokemon to species)
        } catch (err: Exception) {
            Log.e("PokemonDetailViewModel", err.localizedMessage, err)
            emit(null)
        }
    }
}