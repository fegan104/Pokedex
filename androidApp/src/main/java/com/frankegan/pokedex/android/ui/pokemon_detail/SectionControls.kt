package com.frankegan.pokedex.android.ui.pokemon_detail

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frankegan.pokedex.android.R
import com.frankegan.pokedex.android.ui.common.TypeButton
import com.frankegan.pokedex.model.Move
import com.frankegan.pokedex.model.Pokemon

private enum class Selection(@StringRes val displayName: Int) {
    Stats(R.string.stats),
    Evolutions(R.string.evolutions),
    Moves(R.string.moves)
}

@Composable
fun DetailSectionControls(
    pokemon: Pokemon,
    moves: List<Move>,
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
                PokemonMovesList(moves)
            }
        }
    }
}

@Composable
fun PokemonMovesList(moves: List<Move>) {
    val englishDisplayNames = remember {
        moves.map { move ->
            move.displayNames.first { it.language.name == "en" }.displayName
        }
    }
    Column(Modifier.wrapContentHeight().padding(bottom = 72.dp)) {
        for(index in moves.indices) {
            MoveRow(englishDisplayNames[index], moves[index])
        }
    }
}

@Composable
fun MoveRow(displayName: String, move: Move) {
    var showFlavorText by remember { mutableStateOf(false) }
    val englishFlavorText = remember {
        move.flavorTextEntries.first { it.language.name == "en" }.flavorText
    }
    val flavorTextHeight = if (showFlavorText) {
        Modifier.wrapContentHeight()
    } else {
        Modifier.height(0.dp)
    }

    Column(
        Modifier
            .animateContentSize()
            .clickable {
                showFlavorText = !showFlavorText
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(72.dp)
                .padding(16.dp)
        ) {
            Text(text = displayName, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            TypeButton(type = move.type.name)
        }
        Text(
            text = englishFlavorText.replace("[\\t\\n\\f]+".toRegex(), " "),
            flavorTextHeight
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp)
        )
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier
                .alpha(0.6f)
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )
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
                    text = stat.stat.shortName,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxWidth(0.125f)
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
                    progress = stat.baseStatAsPercentageOfMax,
                    color = color,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxWidth(0.775f)
                        .clip(RoundedCornerShape(8.dp))
                        .height(8.dp)
                )
            }
        }
    }
}
