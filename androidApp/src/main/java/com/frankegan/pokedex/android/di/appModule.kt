package com.frankegan.pokedex.android.di

import com.frankegan.pokedex.android.domain.GetPokemonPageUseCase
import com.frankegan.pokedex.android.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { GetPokemonPageUseCase(get()) }
    viewModel { HomeViewModel(get()) }
}