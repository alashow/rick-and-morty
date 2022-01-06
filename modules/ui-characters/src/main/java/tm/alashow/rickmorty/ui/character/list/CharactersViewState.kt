/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.list

data class CharactersViewState(
    val error: Throwable? = null,
) {

    companion object {
        val EMPTY = CharactersViewState()
    }
}
