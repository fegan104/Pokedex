package com.frankegan.pokedex.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.frankegan.pokedex.android.ui.NavRoute
import com.frankegan.pokedex.android.ui.home.HomeScreen
import com.frankegan.pokedex.android.ui.home.HomeViewModel
import com.frankegan.pokedex.android.ui.pokemon_detail.PokemonDetailScreen
import com.frankegan.pokedex.android.ui.theme.PokedexTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which means we need to through handling
        // insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Update the system bars to be translucent
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
            }

            val navController = rememberNavController()
            val lazyPagingItems = homeViewModel.getPokemon().collectAsLazyPagingItems()

            PokedexTheme {
                ProvideWindowInsets {
                    NavHost(navController = navController, startDestination = NavRoute.Home.route) {
                        composable(NavRoute.Home.route) { HomeScreen(lazyPagingItems, navController) }
                        composable(
                            NavRoute.PokemonDetail.route,
                            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            PokemonDetailScreen(
                                getViewModel(),
                                backStackEntry.arguments?.getInt("pokemonId")
                            )
                        }
                    }
                }
            }
        }
    }
}