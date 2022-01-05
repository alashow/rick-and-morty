/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tm.alashow.navigation.screens.CHARACTER_ID_KEY
import tm.alashow.rickmorty.data.interactors.character.GetCharacterDetails
import tm.alashow.rickmorty.data.observers.character.ObserveCharacter
import tm.alashow.rickmorty.data.observers.character.ObserveCharacterDetails

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val character: ObserveCharacter,
    private val characterDetails: ObserveCharacterDetails
) : ViewModel() {

    private val characterParams = handle.get<Long>(CHARACTER_ID_KEY) ?: 1 // TODO: require default id

    val characterDetailsState = characterDetails.asyncFlow

    init {
        load()
        // TODO: remove this
        viewModelScope.launch {
            (1L..826L).forEach { id ->
                character(id)
                characterDetails(GetCharacterDetails.Params(id))
                delay(5000)
            }
        }
    }

    private fun load(forceRefresh: Boolean = false) {
        character(characterParams)
        if (forceRefresh)
            characterDetails(GetCharacterDetails.Params(-1))
        characterDetails(GetCharacterDetails.Params(characterParams, forceRefresh))
    }

    fun refresh() = load(true)
}
