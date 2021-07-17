package com.frankegan.pokedex.android.ui.pokemon_detail

import android.content.res.Configuration
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.frankegan.pokedex.android.R
import com.frankegan.pokedex.data.*
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.rememberDrawablePainter
import com.google.accompanist.insets.LocalWindowInsets

@Composable
fun PokemonDetailScreen(viewModel: PokemonDetailViewModel, pokemonId: Int?) {
    val pokemonAndSpecies by viewModel.pokemonAndSpecies(pokemonId).observeAsState()
    val (pokemon, species) = pokemonAndSpecies ?: (null to null)

    Scaffold { contentPadding ->
        Box(Modifier.padding(contentPadding)) {
            if (pokemon == null || species == null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No Pokemon Found")
                }
            } else {
                PokemonDetailContent(pokemon, species)
            }
        }
    }
}

@Composable
private fun BackgroundHeader(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary
) {
    Surface(
        color = color,
        modifier = modifier,
        content = { }
    )
}

@Composable
private fun PokemonDetailContent(pokemon: Pokemon, species: PokemonSpecies) {
    val typeThemeColor = Color(pokemon.types.first().color)
    val configuration = LocalConfiguration.current
    val bannerHeight = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> 80.dp
        else -> 200.dp
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (imageRef, bottomCardRef) = createRefs()

        BackgroundHeader(
            modifier = Modifier
                .requiredHeight(bannerHeight)
                .fillMaxWidth(),
            color = typeThemeColor
        )

        Surface(
            shape = RoundedCornerShape(24.dp).copy(
                bottomStart = CornerSize(0),
                bottomEnd = CornerSize(0)
            ),
            modifier = Modifier
                .padding(top = bannerHeight - 20.dp)
                .fillMaxSize()
                .constrainAs(bottomCardRef) { }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {

                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Button(
                    shape = CircleShape,
                    onClick = {  },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = typeThemeColor)
                ) {
                    Text(text = pokemon.types.first().type.name.uppercase())
                }

                Text(
                    text = species.flavorTextEntries
                        .firstOrNull()
                        ?.flavorText
                        ?.replace("[\\t\\n\\f]+".toRegex(), " ")
                        ?: species.flavorTextEntries.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp)
                        .padding(horizontal = 24.dp)
                )

                DetailSectionControls(
                    selectedColors = ButtonDefaults.buttonColors(backgroundColor = typeThemeColor),
                    unSelectedColors = ButtonDefaults.textButtonColors(contentColor = typeThemeColor),
                )
            }
        }

        Image(
            painter = rememberCoilPainter(
                request = pokemon.sprites.frontDefault,
                previewPlaceholder = R.drawable.empty_pokemon
            ),
            contentDescription = pokemon.name,
            modifier = Modifier
                .height(bannerHeight)
                .width(bannerHeight)
                .constrainAs(imageRef) {
                    bottom.linkTo(bottomCardRef.top, margin = -(bannerHeight + 40.dp))
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Preview(widthDp = 500, heightDp = 1000, showBackground = true)
@Composable
private fun PokemonDetailCardPreview() {
    val bulbasaur = Pokemon(
        id = 1,
        name = "Bulbasaur",
        height = 1,
        weight = 1,
        species = NamedApiResource(name = "Bulbasaur", ""),
        types = listOf(
            PokemonType(1, NamedApiResource(name = "Grass", ""))
        ),
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

    val species = PokemonSpecies(
        id = 1,
        name = "Bulbasaur",
        color = NamedApiResource(name = "green", ""),
        generation = NamedApiResource(name = "i", ""),
        flavorTextEntries = listOf(
            PokemonSpeciesFlavorText(
                language = NamedApiResource(name = "en", ""),
                version = NamedApiResource(name = "version", ""),
                flavorText = "A strange seed was planted on its back at birth. The plant sprouts and grows with this POKéMON."
            )
        )
    )
    PokemonDetailContent(pokemon = bulbasaur, species = species)
}