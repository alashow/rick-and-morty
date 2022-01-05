/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

import tm.alashow.rickmorty.domain.entities.CharacterId

data class CharacterParams(
    val id: CharacterId,
    val page: Int = 0,
) {

    // used in Room queries
    override fun toString() = "id=$id"

    companion object {
        fun CharacterParams.toQueryMap(): Map<String, Any> = mutableMapOf<String, Any>(
            "page" to page,
        )
    }
}
