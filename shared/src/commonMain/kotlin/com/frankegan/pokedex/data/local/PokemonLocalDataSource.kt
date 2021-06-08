package com.frankegan.pokedex.data.local

import com.frankegan.pokedex.data.NamedApiResource
import com.frankegan.pokedex.data.Pokemon
import com.frankegan.pokedex.data.PokemonDataSource
import com.frankegan.pokedex.data.PokemonDataSource.Companion.PAGE_SIZE
import com.frankegan.pokedex.data.PokemonSpecies
import com.frankegan.pokedex.data.PokemonSpeciesFlavorText
import com.frankegan.pokedex.data.PokemonSprites
import com.frankegan.pokedex.data.PokemonType

class PokemonLocalDataSource(databaseDriverFactory: DatabaseDriverFactory) : PokemonDataSource {

    private val appDatabase by lazy { AppDatabase(databaseDriverFactory.createDriver()) }

    override suspend fun getPokemonPagedResult(page: Int): Result<List<Pokemon>> = runCatching {
        val pokemonQuery =
            appDatabase.pokemonQueries.selectByIdBetween(page * PAGE_SIZE + 1, (page + 1) * PAGE_SIZE) { id, name, speciesName, speciesUrl, height, weight ->

                val types = queryTypes(id)

                val sprites = querySprites(id)

                Pokemon(
                    id = id,
                    name = name,
                    height = height,
                    weight = weight,
                    species = NamedApiResource(name = speciesName, url = speciesUrl),
                    types,
                    sprites
                )
            }

        pokemonQuery.executeAsList().throwIfEmpty()
    }

    override suspend fun getPokemonSpecies(id: Int): Result<PokemonSpecies> {
        val pokemonSpeciesQuery =
            appDatabase.pokemonSpeciesQueries.selectSpeciesByPokemonId(id) { id, name, colorName, colorUrl, generationName, generationUrl ->
                val flavorTextEntries =
                    appDatabase.pokemonSpeciesQueries.selectFlavorTextByPokemonId(id) { _, flavorText, versionName, versionUrl, languageName, languageUrl ->
                        PokemonSpeciesFlavorText(
                            flavorText = flavorText,
                            language = NamedApiResource(name = languageName, url = languageUrl),
                            version = NamedApiResource(name = versionName, url = versionUrl)
                        )
                    }.executeAsList()

                PokemonSpecies(
                    id = id,
                    name = name,
                    color = NamedApiResource(name = colorName, colorUrl),
                    generation = NamedApiResource(name = generationName, url = generationUrl),
                    flavorTextEntries = flavorTextEntries
                )
            }

        return runCatching { pokemonSpeciesQuery.executeAsOne() }
    }

    override suspend fun savePokemon(pokemon: Pokemon): Result<Pokemon> = runCatching {

        appDatabase.pokemonQueries.insert(
            id = pokemon.id,
            name = pokemon.name,
            speciesName = pokemon.species.name,
            speciesUrl = pokemon.species.url,
            height = pokemon.height,
            weight = pokemon.weight
        )

        appDatabase.pokemonSpriteQueries.insert(
            pokemonId = pokemon.id,
            backDefault = pokemon.sprites.backDefault,
            backShiny = pokemon.sprites.backShiny,
            frontDefault = pokemon.sprites.frontDefault,
            frontShiny = pokemon.sprites.frontShiny,
            backFemale = pokemon.sprites.backFemale,
            backShinyFemale = pokemon.sprites.backShinyFemale,
            frontFemale = pokemon.sprites.frontFemale,
            frontShinyFemale = pokemon.sprites.frontShinyFemale
        )

        for (pokemonType in pokemon.types) {
            appDatabase.pokemonTypeQueries.insert(
                pokemonId = pokemon.id,
                slot = pokemonType.slot,
                name = pokemonType.type.name,
                url = pokemonType.type.url
            )
        }

        pokemon
    }

    override suspend fun savePokemonSpecies(species: PokemonSpecies): Result<PokemonSpecies> = runCatching {
        appDatabase.pokemonSpeciesQueries.insertPokemonSpecies(
            id = species.id,
            name = species.name,
            colorName = species.color.name,
            colorUrl = species.color.url,
            generationName = species.generation.name,
            generationUrl = species.generation.url
        )

        species
    }

    private fun querySprites(pokemonId: Int): PokemonSprites {
        return appDatabase.pokemonSpriteQueries.selectByPokemonId(pokemonId) { _, backDefault, backShiny, frontDefault, frontShiny, backFemale, backShinyFemale, frontFemale, frontShinyFemale ->
            PokemonSprites(backDefault, backShiny, frontDefault, frontShiny, backFemale, backShinyFemale, frontFemale, frontShinyFemale)
        }.executeAsOne()
    }

    private fun queryTypes(pokemonId: Int): List<PokemonType> {
        return appDatabase.pokemonTypeQueries.selectByPokemonId(pokemonId) { slot, typeName, url ->
            PokemonType(slot, type = NamedApiResource(name = typeName, url = url))
        }.executeAsList()
    }
}

class DataNotFoundError : Error("No data found")

private fun <T> List<T>.throwIfEmpty(): List<T> {
    return when {
        isEmpty() -> throw DataNotFoundError()
        else -> this
    }
}