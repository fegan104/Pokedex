package com.frankegan.pokedex.android.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.frankegan.pokedex.model.PokemonType
import com.frankegan.pokedex.model.TypeName
import com.frankegan.pokedex.model.TypeResource

@Composable
fun TypeButton(
    type: TypeName,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    val typeColor = remember { Color(type.color) }

    Button(
        shape = CircleShape,
        onClick = onClick,
        modifier = modifier.padding(top = 8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = typeColor)
    ) {
        Text(text = type.name.uppercase(),)
    }
}

@Preview
@Composable
private fun Preview() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TypeName.values().forEach {
            TypeButton(type = it)
        }
    }
}