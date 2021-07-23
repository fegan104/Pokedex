package com.frankegan.pokedex.data.local

import com.frankegan.pokedex.data.*
import com.frankegan.pokedex.data.PokemonDataSource.Companion.PAGE_SIZE
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PokemonLocalDataSource(
    databaseDriverFactory: DatabaseDriverFactory,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : PokemonDataSource {

    private val appDatabase by lazy { AppDatabase(databaseDriverFactory.createDriver()) }

    override suspend fun getPokemonPage(page: Int): List<Pokemon> {
        return appDatabase.transactionWithContext(dispatcher) {
            val pokemonQuery = pokemonQueries.selectByIdBetween(
                page * PAGE_SIZE + 1,
                (page + 1) * PAGE_SIZE
            ) { id, name, speciesName, speciesUrl, height, weight ->

                val types = queryTypes(id)

                val sprites = querySprites(id)

                val stats = queryStats(id)

                Pokemon(
                    id = id,
                    name = name,
                    height = height,
                    weight = weight,
                    species = NamedApiResource(name = speciesName, url = speciesUrl),
                    types,
                    sprites,
                    stats
                )
            }

            pokemonQuery.executeAsList().throwIfEmpty()
        }
    }

    override suspend fun getPokemon(pokemonId: Int): Pokemon {
        return appDatabase.transactionWithContext(dispatcher) {
            pokemonQueries.selectById(pokemonId) { id, name, speciesName, speciesUrl, height, weight ->
                val types = queryTypes(id)

                val sprites = querySprites(id)

                val stats = queryStats(id)

                Pokemon(
                    id = id,
                    name = name,
                    height = height,
                    weight = weight,
                    species = NamedApiResource(name = speciesName, url = speciesUrl),
                    types,
                    sprites,
                    stats
                )

            }
        }.executeAsOne()
    }

    override suspend fun getPokemonSpecies(id: Int): PokemonSpecies {
        return appDatabase.transactionWithContext(dispatcher) {
            val pokemonSpeciesQuery =
                pokemonSpeciesQueries.selectSpeciesByPokemonId(id) { _, name, colorName, colorUrl, generationName, generationUrl ->
                    val flavorTextEntries =
                        pokemonSpeciesQueries.selectFlavorTextByPokemonId(id) { _, flavorText, versionName, versionUrl, languageName, languageUrl ->
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

            pokemonSpeciesQuery.executeAsOne()
        }
    }

    override suspend fun savePokemon(pokemon: Pokemon): Pokemon {

        return appDatabase.transactionWithContext(dispatcher) {
            pokemonQueries.insert(
                id = pokemon.id,
                name = pokemon.name,
                speciesName = pokemon.species.name,
                speciesUrl = pokemon.species.url,
                height = pokemon.height,
                weight = pokemon.weight
            )

            pokemonSpriteQueries.insert(
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
                pokemonTypeQueries.insert(
                    pokemonId = pokemon.id,
                    slot = pokemonType.slot,
                    name = pokemonType.type.name,
                    url = pokemonType.type.url
                )
            }

            for (stat in pokemon.stats) {
                pokemonStatsQueries.insert(
                    pokemonId = pokemon.id,
                    baseStat = stat.baseStat,
                    effort = stat.effort,
                    name = stat.stat.name,
                    url = stat.stat.url
                )
            }

            return@transactionWithContext pokemon
        }
    }

    override suspend fun savePokemonSpecies(species: PokemonSpecies): PokemonSpecies {

        return appDatabase.transactionWithContext(dispatcher) {
            pokemonSpeciesQueries.insertPokemonSpecies(
                id = species.id,
                name = species.name,
                colorName = species.color.name,
                colorUrl = species.color.url,
                generationName = species.generation.name,
                generationUrl = species.generation.url
            )


            for (flavorText in species.flavorTextEntries) {
                pokemonSpeciesQueries.insertFlavorText(
                    pokemonSpeciesId = species.id,
                    flavorText = flavorText.flavorText,
                    versionName = flavorText.version.name,
                    versionUrl = flavorText.version.url,
                    languageName = flavorText.language.name,
                    languageUrl = flavorText.language.url
                )
            }

            species
        }
    }

    private fun querySprites(pokemonId: Int): PokemonSprites {
        return appDatabase.pokemonSpriteQueries.selectByPokemonId(pokemonId) { _, backDefault, backShiny, frontDefault, frontShiny, backFemale, backShinyFemale, frontFemale, frontShinyFemale ->
            PokemonSprites(
                backDefault,
                backShiny,
                frontDefault,
                frontShiny,
                backFemale,
                backShinyFemale,
                frontFemale,
                frontShinyFemale
            )
        }.executeAsOne()
    }

    private fun queryTypes(pokemonId: Int): List<PokemonType> {
        return appDatabase.pokemonTypeQueries.selectByPokemonId(pokemonId) { slot, typeName, url ->
            PokemonType(slot, type = NamedApiResource(name = typeName, url = url))
        }.executeAsList()
    }

    private fun queryStats(pokemonId: Int): List<PokemonStats> {
        return appDatabase.pokemonStatsQueries.selectById(pokemonId) { _, baseStat, effort, stat_name, stat_url ->
            PokemonStats(
                baseStat,
                effort,
                NamedApiResource(stat_name, stat_url)
            )
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