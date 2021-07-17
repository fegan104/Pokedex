package com.frankegan.pokedex.android.ui

sealed class NavRoute(val route: String) {
    object Home : NavRoute("home")
    object PokemonDetail : NavRoute("pokemon/{pokemonId}") {
        fun createRoute(pokemonId: Int): String = "pokemon/$pokemonId"
    }
}