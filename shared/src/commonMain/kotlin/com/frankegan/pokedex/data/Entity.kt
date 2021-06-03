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


@Serializable
data class PokemonType(
    val slot: Int,
    val type: NamedApiResource
)

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