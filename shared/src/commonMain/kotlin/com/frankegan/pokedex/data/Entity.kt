package com.frankegan.pokedex.data

import com.frankegan.pokedex.data.remote.model.ResourceSummaryList
import com.frankegan.pokedex.data.remote.model.urlToCat
import com.frankegan.pokedex.data.remote.model.urlToId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val species: NamedApiResource,
    val types: List<PokemonType>,
    val sprites: PokemonSprites
) {
    val formattedNumber: String
        get() = "#${id.toString().padStart(3, '0')}"
}

interface ResourceSummary {

    val id: Int
    val category: String
}

@Serializable
data class NamedApiResource(
    val name: String,
    val url: String
) : ResourceSummary {

    override val category: String
        get() = url.urlToCat()

    override val id: Int
        get() = url.urlToId()
}

@Serializable
data class NamedApiResourceList(
    override val count: Int,
    override val next: String?,
    override val previous: String?,
    override val results: List<NamedApiResource>
) : ResourceSummaryList<NamedApiResource>


private val typeNameToTypeColor = mapOf(
    "normal" to 0xFFA8A87B,
    "fighting" to 0xFFBE322E,
    "flying" to 0xFFA893EE,
    "poison" to 0xFF9F449F,
    "ground" to 0xFFD9BA6B,
    "rock" to 0xFFB79F41,
    "bug" to 0xFFA8B732,
    "ghost" to 0xFF705A97,
    "steel" to 0xFFB8B9CF,
    "fire" to 0xFFEE803B,
    "water" to 0xFF6A92ED,
    "grass" to 0xFF7BC757,
    "electric" to 0xFFF7CF43,
    "psychic" to 0xFFF65B89,
    "ice" to 0xFF9AD8D8,
    "dragon" to 0xFF7043F4,
    "dark" to 0xFF705849,
    "fairy" to 0xFFED9AAD
)

@Serializable
data class PokemonType(
    val slot: Int,
    val type: NamedApiResource
) {
    val color: Long = typeNameToTypeColor[type.name]!!
}

@Serializable
data class PokemonSprites(
    @SerialName("back_default")
    val backDefault: String?,
    @SerialName("back_shiny")
    val backShiny: String?,
    @SerialName("front_default")
    val frontDefault: String?,
    @SerialName("front_shiny")
    val frontShiny: String?,
    @SerialName("back_female")
    val backFemale: String?,
    @SerialName("back_shiny_female")
    val backShinyFemale: String?,
    @SerialName("front_female")
    val frontFemale: String?,
    @SerialName("front_shiny_female")
    val frontShinyFemale: String?
)

@Serializable
data class PokemonSpecies(
    val id: Int,
    val name: String,
    val color: NamedApiResource,
    val generation: NamedApiResource,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<PokemonSpeciesFlavorText>
)

@Serializable
data class PokemonSpeciesFlavorText(
    @SerialName("flavor_text")
    val flavorText: String,
    val language: NamedApiResource,
    val version: NamedApiResource
)