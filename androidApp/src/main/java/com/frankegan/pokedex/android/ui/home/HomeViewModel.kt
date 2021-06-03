package com.frankegan.pokedex.android.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.frankegan.pokedex.data.PokemonRepository
import com.frankegan.pokedex.data.local.DatabaseDriverFactory

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val databaseFactory = DatabaseDriverFactory(app)
    private val pokemonRepo: PokemonRepository = PokemonRepository(databaseFactory)

    fun getPokemon() = liveData {
        val nextPokemonPage = pokemonRepo.getPokemonPagedResult(0)
        emit(nextPokemonPage)
    }
}