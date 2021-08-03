package com.frankegan.pokedex.data.local

import com.frankegan.pokedex.data.*
import com.frankegan.pokedex.data.PokemonDataSource.Companion.PAGE_SIZE
import com.frankegan.pokedex.data.local.model.PokemonMove
import com.frankegan.pokedex.model.*
import com.squareup.sqldelight.EnumColumnAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.frankegan.pokedex.data.local.model.PokemonStats.Adapter as PokemonStatsAdapter
import com.frankegan.pokedex.data.local.model.PokemonType.Adapter as PokemonTypeAdapter

class PokemonLocalDataSource(
    databaseDriverFactory: DatabaseDriverFactory,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : PokemonDataSource {

    private val appDatabase by lazy {
        AppDatabase(
            databaseDriverFactory.createDriver(),
            pokemonMoveAdapter = PokemonMove.Adapter(
                typeNameAdapter = EnumColumnAdapter()
            ),
            pokemonTypeAdapter = PokemonTypeAdapter(
                nameAdapter = EnumColumnAdapter()
            ),
            pokemonStatsAdapter = PokemonStatsAdapter(
                nameAdapter = EnumColumnAdapter()
            )
        )
    }

    override suspend fun getPokemonPage(page: Int): List<Pokemon> {
        return appDatabase.transactionWithContext(dispatcher) {
            val pokemonQuery = pokemonQueries.selectByIdBetween(
                page * PAGE_SIZE + 1,
                (page + 1) * PAGE_SIZE
            ) { id, name, speciesName, speciesUrl, height, weight ->

                val types = queryTypes(id)

                val sprites = querySprites(id)

                val stats = queryStats(id)

                val moves = queryMoveResources(id)

                Pokemon(
                    id = id,
                    name = name,
                    height = height,
                    weight = weight,
                    species = NamedApiResource(name = speciesName, url = speciesUrl),
                    types,
                    sprites,
                    stats,
                    moves
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

                val moves = queryMoveResources(id)

                Pokemon(
                    id = id,
                    name = name,
                    height = height,
                    weight = weight,
                    species = NamedApiResource(name = speciesName, url = speciesUrl),
                    types,
                    sprites,
                    stats,
                    moves
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

            for (move in pokemon.moves) {
                pokemonMoveQueries.inertPokemonToMove(
                    pokemon.id,
                    move.move.id,
                    move.move.name,
                    move.move.url
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

    override suspend fun getMoves(pokemonId: Int): List<Move> {
        return appDatabase.transactionWithContext(dispatcher) {
            val numMoves = pokemonMoveQueries.selectPokemonMoves(pokemonId).executeAsList().size
            pokemonMoveQueries.selectMoves(pokemonId)
                .executeAsList()
                .groupBy(
                    keySelector = {
                        Move(
                            id = it.moveId,
                            name = it.moveName,
                            displayNames = emptyList(),
                            accuracy = it.accuracy,
                            pp = it.pp,
                            priority = it.priority!!,
                            power = it.power,
                            damageClass = NamedApiResource(
                                it.damageClassName!!,
                                it.damageClassUrl!!
                            ),
                            flavorTextEntries = emptyList(),
                            type = TypeResource(it.typeName!!, it.typeUrl!!)
                        )
                    },
                    valueTransform = {
                        MoveFlavorText(
                            it.flavorText!!,
                            NamedApiResource(it.languageName!!, it.languageUrl!!)
                        ) to MoveName(
                            language = NamedApiResource(it.languageName, it.languageName),
                            displayName = it.displayName!!
                        )
                    })
                .map { moveToFlavorText ->
                    val (move, flavorTextAndMoveName) = moveToFlavorText
                    move.copy(
                        flavorTextEntries = flavorTextAndMoveName.map { it.first },
                        displayNames = flavorTextAndMoveName.map { it.second }
                    )
                }.throwIfNotSize(numMoves)
        }
    }

    override suspend fun saveMoves(moves: List<Move>): List<Move> {
        return appDatabase.transactionWithContext(dispatcher) {
            for (move in moves) {
                pokemonMoveQueries.inertMove(
                    move.id,
                    move.name,
                    move.accuracy,
                    move.pp,
                    move.priority,
                    move.power,
                    move.damageClass.name,
                    move.damageClass.url,
                    move.type.name,
                    move.type.url
                )

                for (flavorText in move.flavorTextEntries) {
                    pokemonMoveQueries.insertMoveFlavorText(
                        move.id,
                        flavorText.flavorText,
                        flavorText.language.name,
                        flavorText.language.url
                    )
                }

                for (name in move.displayNames) {
                    pokemonMoveQueries.insertMoveName(
                        move.id,
                        displayName = name.displayName,
                        languageName = name.language.name,
                        languageUrl = name.language.url
                    )
                }
            }
            moves
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
            PokemonType(slot, type = TypeResource(name = typeName, url = url))
        }.executeAsList()
    }

    private fun queryStats(pokemonId: Int): List<PokemonStats> {
        return appDatabase.pokemonStatsQueries.selectById(pokemonId) { _, baseStat, effort, stat_name, stat_url ->
            PokemonStats(
                baseStat,
                effort,
                StatResource(stat_name, stat_url)
            )
        }.executeAsList()
    }

    private fun queryMoveResources(pokemonId: Int): List<MoveResource> {
        return appDatabase.pokemonMoveQueries.selectPokemonMoves(pokemonId) { moveName, moveUrl ->
            MoveResource(moveName, moveUrl)
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

private fun <T> List<T>.throwIfNotSize(size: Int): List<T> {
    return when {
        this.size != size -> throw DataNotFoundError()
        else -> this
    }
}