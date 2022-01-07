/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharactersParams(
    // example: Rick
    val name: String? = null,
    // options: alive, dead or unknown
    val status: String? = null,
    // examples: Human, Alien
    val species: String? = null,
    // examples: Bird-Person
    val type: String? = null,
    // options: female, male, genderless or unknown
    val gender: String? = null,
    val page: Int = 0,

    val filters: Filters = Filters(),
) {

    @Serializable
    data class FilterOptions(
        val statusOptions: Set<String> = emptySet(),
        val speciesOptions: Set<String> = emptySet(),
        val typeOptions: Set<String> = emptySet(),
        val genderOptions: Set<String> = emptySet(),
        val originOptions: Set<String> = emptySet(),
        val locationOptions: Set<String> = emptySet(),
    )

    @Serializable
    data class Filters(
        val status: String? = null,
        val species: String? = null,
        val type: String? = null,
        val gender: String? = null,
        val origin: String? = null,
        val location: String? = null,
    ) {
        val hasFilters = status != null || species != null || type != null || gender != null || origin != null || location != null
    }

    // used as a key in Room/Store
    override fun toString() = "nm=$name,st=$status,sp=$species,tp=$type,gn=$gender"

    companion object {
        fun CharactersParams.toQueryMap(): Map<String, Any> = mutableMapOf<String, Any>(
            "name" to (name ?: ""),
            "status" to (status ?: ""),
            "species" to (species ?: ""),
            "type" to (type ?: ""),
            "gender" to (gender ?: ""),
            "page" to page + 1,
        )
    }
}
