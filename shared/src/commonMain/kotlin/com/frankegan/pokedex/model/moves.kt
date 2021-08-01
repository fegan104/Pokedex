package com.frankegan.pokedex.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Move(
    val id: Int,
    val name: String,
    @SerialName("names")
    val displayNames: List<MoveName>,
    val accuracy: Int?,
    val pp: Int?,
    val priority: Int,
    val power: Int?,
    @SerialName("damage_class")
    val damageClass: NamedApiResource,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<MoveFlavorText>,
    val type: TypeResource
)

@Serializable
data class MoveFlavorText(
    @SerialName("flavor_text")
    val flavorText: String,
    val language: NamedApiResource,
    @SerialName("version_group")
    val versionGroup: NamedApiResource
)

@Serializable
data class MoveResource(
    val move: NamedApiResource
) {
    constructor(moveName: String, moveUrl: String) : this(
        NamedApiResource(moveName, moveUrl)
    )
}

@Serializable
data class MoveName(
    val language: NamedApiResource,
    @SerialName("name")
    val moveName: String
)