package com.frankegan.pokedex.model

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
    val sprites: PokemonSprites,
    val stats: List<PokemonStats>,
    val moves: List<MoveResource>
) {
    val formattedNumber: String
        get() = "#${id.toString().padStart(3, '0')}"
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

@Serializable
data class PokemonStats(
    @SerialName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatResource
) {
    val baseStatAsPercentageOfMax: Float = baseStat / 255f
}

@Serializable
data class StatResource(
    val name: StatName,
    val url: String
) {
    val shortName: String inline get() = name.shortName
}

@Serializable
enum class StatName {
    @SerialName("hp")
    HP {
        override val shortName: String = "HP"
    },

    @SerialName("attack")
    Attack {
        override val shortName: String = "ATK"
    },

    @SerialName("defense")
    Defense {
        override val shortName: String = "DEF"
    },

    @SerialName("special-attack")
    SpecialAttack {
        override val shortName: String = "SATK"
    },

    @SerialName("special-defense")
    SpecialDefense {
        override val shortName: String = "SDEF"
    },

    @SerialName("speed")
    Speed {
        override val shortName: String = "SPD"
    };

    abstract val shortName: String
}
