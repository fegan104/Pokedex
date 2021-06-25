package com.frankegan.pokedex.android.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.frankegan.pokedex.data.NamedApiResource
import com.frankegan.pokedex.data.Pokemon
import com.frankegan.pokedex.data.PokemonSprites
import com.google.accompanist.coil.rememberCoilPainter
import java.util.*

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    Column {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp
        ) {
            Text(
                text = "Pokédex",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        val lazyPagingItems = viewModel.getPokemon().collectAsLazyPagingItems()

        LazyColumn {
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
                PokemonRow(pokemon)
            }
        }
    }
}

@Composable
fun PokemonRow(pokemon: Pokemon) {
    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
        modifier = Modifier
            .height(100.dp)
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { }
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
                text = pokemon.name,
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
fun PokemonRowPreview() {
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
        )
    )
    PokemonRow(pokemon = bulbasaur)
}