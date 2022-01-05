/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tm.alashow.navigation.screens.CHARACTER_ID_KEY
import tm.alashow.rickmorty.data.CharacterParams
import tm.alashow.rickmorty.data.interactors.character.GetCharacterDetails
import tm.alashow.rickmorty.data.observers.character.ObserveCharacter
import tm.alashow.rickmorty.data.observers.character.ObserveCharacterDetails
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val character: ObserveCharacter,
    private val characterDetails: ObserveCharacterDetails
) : ViewModel() {

    private val characterParams = CharacterParams(handle.get<String>(CHARACTER_ID_KEY) ?: "todo-non-nullable")

    init {
        load()
    }

    private fun load(forceRefresh: Boolean = false) {
        character(characterParams)
        characterDetails(GetCharacterDetails.Params(characterParams, forceRefresh))
    }

    fun refresh() = load(true)
}
