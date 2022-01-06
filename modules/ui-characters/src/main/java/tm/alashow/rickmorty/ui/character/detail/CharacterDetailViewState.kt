/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.detail

import tm.alashow.domain.models.*
import tm.alashow.rickmorty.domain.entities.Character

data class CharacterDetailViewState(
    val character: Character? = null,
    val characterDetails: Async<Character> = Uninitialized,
) {
    val isLoaded = character != null
    val isDetailsLoading = characterDetails is Loading

    companion object {
        val EMPTY = CharacterDetailViewState()
    }
}
