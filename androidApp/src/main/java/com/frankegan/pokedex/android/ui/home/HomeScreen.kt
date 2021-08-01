package com.frankegan.pokedex.android.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.frankegan.pokedex.android.ui.NavRoute
import com.frankegan.pokedex.model.*
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.rememberInsetsPaddingValues
import java.util.*

@Composable
fun HomeScreen(lazyPagingItems: LazyPagingItems<Pokemon>, navController: NavController) {
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                contentPadding = rememberInsetsPaddingValues(
                    LocalWindowInsets.current.statusBars,
                    applyBottom = false,
                ),
                elevation = 0.dp
            ) {
                Text(
                    text = "Pokédex",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        bottomBar = {
            // We add a spacer as a bottom bar, which is the same height as
            // the navigation bar
            Spacer(
                Modifier
                    .navigationBarsHeight()
                    .fillMaxWidth()
            )
        }
    ) { contentPadding ->
        Box {

            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier.scrollable(scrollState, Orientation.Vertical),
                state = listState,
            ) {
                if (lazyPagingItems.loadState.refresh == LoadState.Loading
                    || lazyPagingItems.loadState.append == LoadState.Loading
                ) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }

                items(lazyPagingItems) { pokemon ->
                    pokemon ?: return@items
                    PokemonRow(
                        pokemon,
                        onClick = {
                            navController.navigate(NavRoute.PokemonDetail.createRoute(pokemon.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PokemonRow(pokemon: Pokemon, onClick: () -> Unit) {
    val cardShape = remember { RoundedCornerShape(12.dp) }
    Card(
        elevation = 0.dp,
        shape = cardShape,
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        modifier = Modifier
            .height(96.dp)
            .padding(8.dp)
            .fillMaxWidth()
            .clip(cardShape)
            .clickable { onClick() }
    ) {
        ConstraintLayout {
            val (number, name, sprite) = createRefs()

            Text(
                text = pokemon.formattedNumber,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(number) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 16.dp)
                }
            )
            Text(
                text = pokemon.name.replaceFirstChar { it.titlecase() },
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(name) {
                    start.linkTo(number.end, margin = 16.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            Image(
                painter = rememberCoilPainter(pokemon.sprites.frontDefault),
                contentDescription = pokemon.name,
                modifier = Modifier
                    .constrainAs(sprite) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .width(100.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Preview(heightDp = 100)
@Composable
private fun PokemonRowPreview() {
    val bulbasaur = Pokemon(
        id = 1,
        name = "Bulbasaur",
        height = 1,
        weight = 1,
        species = NamedApiResource(name = "Bulbasaur", ""),
        types = listOf(),
        sprites = PokemonSprites(
            backDefault = null,
            backShiny = null,
            frontDefault = null,
            frontShiny = null,
            backFemale = null,
            backShinyFemale = null,
            frontFemale = null,
            frontShinyFemale = null
        ),
        stats = listOf(
            PokemonStats(45, 0, StatResource(StatName.HP, "")),
            PokemonStats(49, 0, StatResource(StatName.Attack, "")),
            PokemonStats(49, 0, StatResource(StatName.Defense, "")),
        ),
        moves = emptyList()
    )
    PokemonRow(pokemon = bulbasaur) { }
}