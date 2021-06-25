package com.frankegan.pokedex.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.frankegan.pokedex.android.ui.home.HomeScreen
import com.frankegan.pokedex.android.ui.home.HomeViewModel
import com.frankegan.pokedex.android.ui.theme.PokedexTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexTheme {
                HomeScreen(viewModel)
            }
        }
    }
}