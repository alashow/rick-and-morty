/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

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
) {

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
