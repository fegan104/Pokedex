package com.frankegan.pokedex.model

data class Name(
    val name: String,
    val language: NamedApiResource
)

data class Description(
    val description: String,
    val language: NamedApiResource
)
