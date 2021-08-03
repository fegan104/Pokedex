package com.frankegan.pokedex.android.ui.pokemon_detail

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.constraintlayout.compose.ConstraintLayout
import com.frankegan.pokedex.android.R
import com.frankegan.pokedex.android.ui.common.TypeButton
import com.frankegan.pokedex.model.*
import com.google.accompanist.coil.rememberCoilPainter


@Composable
fun PokemonDetailScreen(viewModel: PokemonDetailViewModel, pokemonId: Int?) {
    val _uiState by viewModel.uiState(pokemonId).observeAsState()

    Scaffold { contentPadding ->
        Box(Modifier.padding(contentPadding)) {
            when (val uiState = _uiState) {
                is PokemonDetailViewModel.UiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PokemonDetailViewModel.UiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = uiState.msg ?: "Unknown error occurred")
                    }
                }

                is PokemonDetailViewModel.UiState.Data -> {
                    PokemonDetailContent(uiState.pokemon, uiState.species, uiState.moves)
                }
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
private fun PokemonDetailContent(
    pokemon: Pokemon,
    species: PokemonSpecies,
    moves: List<Move>
) {
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
        val scrollState = rememberScrollState()

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
                .padding(top = max(0.dp, bannerHeight - 20.dp - scrollState.value.dp))
                .wrapContentHeight()
                .constrainAs(bottomCardRef) { }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(scrollState)
            ) {

                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(top = 32.dp)
                )

                TypeButton(type = pokemon.types.first().type.name)

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
                    pokemon = pokemon,
                    moves = moves,
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
                .absoluteOffset(y = -scrollState.value.dp)
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
            PokemonType(1, TypeResource(name = TypeName.GRASS, ""))
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
        ),
        stats = listOf(
            PokemonStats(45, 0, StatResource(StatName.HP, "")),
            PokemonStats(49, 0, StatResource(StatName.Attack, "")),
            PokemonStats(49, 0, StatResource(StatName.Defense, "")),
            PokemonStats(49, 0, StatResource(StatName.SpecialAttack, "")),
            PokemonStats(49, 0, StatResource(StatName.SpecialDefense, "")),
            PokemonStats(49, 0, StatResource(StatName.Speed, "")),
        ),
        moves = emptyList()
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
    PokemonDetailContent(pokemon = bulbasaur, species = species, moves = emptyList())
}