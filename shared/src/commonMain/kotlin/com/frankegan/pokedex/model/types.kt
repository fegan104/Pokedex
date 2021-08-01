package com.frankegan.pokedex.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonType(
    val slot: Int,
    val type: TypeResource
) {
    val color: Long inline get() = type.name.color
}

@Serializable
data class TypeResource(
    val name: TypeName,
    val url: String
)

@Serializable
enum class TypeName {

    @SerialName("normal")
    NORMAL {
        override val color: Long = 0xFFA8A87B
    },

    @SerialName("fighting")
    FIGHTING {
        override val color: Long = 0xFFBE322E
    },

    @SerialName("flying")
    FLYING {
        override val color: Long = 0xFFA893EE
    },

    @SerialName("poison")
    POISON {
        override val color: Long = 0xFF9F449F
    },

    @SerialName("ground")
    GROUND {
        override val color: Long = 0xFFD9BA6B
    },

    @SerialName("rock")
    ROCK {
        override val color: Long = 0xFFB79F41
    },

    @SerialName("bug")
    BUG {
        override val color: Long = 0xFFA8B732
    },

    @SerialName("ghost")
    GHOST {
        override val color: Long = 0xFF705A97
    },

    @SerialName("steel")
    STEEL {
        override val color: Long = 0xFFB8B9CF
    },

    @SerialName("fire")
    FIRE {
        override val color: Long = 0xFFEE803B
    },

    @SerialName("water")
    WATER {
        override val color: Long = 0xFF6A92ED
    },

    @SerialName("grass")
    GRASS {
        override val color: Long = 0xFF7BC757
    },

    @SerialName("electric")
    ELECTRIC {
        override val color: Long = 0xFFF7CF43
    },

    @SerialName("psychic")
    PSYCHIC {
        override val color: Long = 0xFFF65B89
    },

    @SerialName("ice")
    ICE {
        override val color: Long = 0xFF9AD8D8
    },

    @SerialName("dragon")
    DRAGON {
        override val color: Long = 0xFF7043F4
    },

    @SerialName("dark")
    DARK {
        override val color: Long = 0xFF705849
    },

    @SerialName("fairy")
    FAIRY {
        override val color: Long = 0xFFED9AAD
    };

    abstract val color: Long
}