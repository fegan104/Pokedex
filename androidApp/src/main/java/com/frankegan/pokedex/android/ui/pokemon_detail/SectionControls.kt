package com.frankegan.pokedex.android.ui.pokemon_detail

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frankegan.pokedex.android.R
import com.frankegan.pokedex.data.Pokemon
import java.util.*

private enum class Selection(@StringRes val displayName: Int) {
    Stats(R.string.stats),
    Evolutions(R.string.evolutions),
    Moves(R.string.moves)
}

@Composable
fun DetailSectionControls(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
    selectedColors: ButtonColors = ButtonDefaults.buttonColors(),
    unSelectedColors: ButtonColors = ButtonDefaults.buttonColors()
) {
    val (selection, setSelection) = remember { mutableStateOf(Selection.Stats) }

    Column(
        modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {

            SelectionControlButton(
                selectionType = Selection.Stats,
                currentSelection = selection,
                onSelect = setSelection,
                selectedColors = selectedColors,
                unSelectedColors = unSelectedColors
            )
            SelectionControlButton(
                selectionType = Selection.Evolutions,
                currentSelection = selection,
                onSelect = setSelection,
                selectedColors = selectedColors,
                unSelectedColors = unSelectedColors
            )
            SelectionControlButton(
                selectionType = Selection.Moves,
                currentSelection = selection,
                onSelect = setSelection,
                selectedColors = selectedColors,
                unSelectedColors = unSelectedColors
            )
        }

        when (selection) {
            Selection.Stats -> {
                PokemonBaseStatsBars(pokemon)
            }
            Selection.Evolutions -> {
                Text(
                    text = """
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                    incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
                    exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure 
                    dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                    Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt
                    ollit anim id est laborum.
                    """.trimIndent(),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 24.dp)
                )
            }
            Selection.Moves -> {
                Text(
                    text = """
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                    incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud 
                    exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure 
                    dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                    Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt
                    ollit anim id est laborum.
                    """.trimIndent(),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun SelectionControlButton(
    selectionType: Selection,
    currentSelection: Selection,
    onSelect: (Selection) -> Unit,
    selectedColors: ButtonColors = ButtonDefaults.buttonColors(),
    unSelectedColors: ButtonColors = ButtonDefaults.buttonColors()
) {

    when (currentSelection) {
        selectionType -> {
            Button(
                onClick = { onSelect(selectionType) },
                shape = CircleShape,
                colors = selectedColors
            ) {
                Text(
                    text = stringResource(selectionType.displayName).uppercase(),
                    Modifier.padding(horizontal = 12.dp)
                )
            }
        }
        else -> {
            TextButton(
                onClick = { onSelect(selectionType) },
                colors = unSelectedColors
            ) {
                Text(
                    text = stringResource(selectionType.displayName).uppercase(),
                    Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
fun PokemonBaseStatsBars(pokemon: Pokemon) {
    val color = remember { Color(pokemon.types.first().color) }

    Column(Modifier.padding(top = 16.dp)) {
        for (stat in pokemon.stats) {
            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stat.stat.name.take(3).uppercase(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(0.1f)
                )
                Text(
                    text = stat.baseStat.toString().padStart(3, '0'),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(0.1f)
                )
                LinearProgressIndicator(
                    progress = stat.baseStat / 255f,
                    color = color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(8.dp))
                        .height(8.dp)
                )
            }
        }
    }
}
