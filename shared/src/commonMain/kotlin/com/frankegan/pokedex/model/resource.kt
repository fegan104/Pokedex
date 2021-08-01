package com.frankegan.pokedex.model

import kotlinx.serialization.Serializable

fun String.urlToId(): Int {
    return "/-?[0-9]+/$".toRegex().find(this)!!.value.filter { it.isDigit() || it == '-' }.toInt()
}

fun String.urlToCat(): String {
    return "/[a-z\\-]+/-?[0-9]+/$".toRegex().find(this)!!.value.filter { it.isLetter() || it == '-' }
}

private fun resourceUrl(id: Int, category: String): String {
    return "/api/v2/$category/$id/"
}

data class ApiResource(
    val url: String
) : ResourceSummary {
    constructor(category: String, id: Int) : this(resourceUrl(id, category))

    override val category by lazy { url.urlToCat() }
    override val id by lazy { url.urlToId() }
}

interface ResourceSummaryList<out T : ResourceSummary> {
    val count: Int
    val next: String?
    val previous: String?
    val results: List<T>
}

data class ApiResourceList(
    override val count: Int,
    override val next: String?,
    override val previous: String?,
    override val results: List<ApiResource>
) : ResourceSummaryList<ApiResource>

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
