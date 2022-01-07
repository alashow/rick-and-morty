/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.list

import tm.alashow.rickmorty.data.CharactersParams

data class CharactersViewState(
    val filterOptions: CharactersParams.FilterOptions = CharactersParams.FilterOptions(),
    val filters: CharactersParams.Filters = CharactersParams.Filters(),
    val error: Throwable? = null,
) {

    companion object {
        val EMPTY = CharactersViewState()
    }
}
