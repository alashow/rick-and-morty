/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

import tm.alashow.rickmorty.domain.entities.CharacterId

data class CharacterParams(
    val id: CharacterId,
) {

    // used as key in Room/Store
    override fun toString() = "id=$id"
}
