/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import tm.alashow.base.ui.SnackbarAction
import tm.alashow.base.ui.SnackbarManager
import tm.alashow.base.ui.SnackbarMessage
import tm.alashow.base.util.extensions.stateInDefault
import tm.alashow.base.util.toUiMessage
import tm.alashow.domain.models.Fail
import tm.alashow.i18n.UiMessage
import tm.alashow.navigation.screens.CHARACTER_ID_KEY
import tm.alashow.rickmorty.data.interactors.character.GetCharacterDetails
import tm.alashow.rickmorty.data.observers.character.ObserveCharacter
import tm.alashow.rickmorty.data.observers.character.ObserveCharacterDetails
import tm.alashow.rickmorty.ui.character.R

data class DetailsFailedToLoadMessage(val error: Throwable) : SnackbarMessage<Throwable>(
    message = error.toUiMessage(),
    action = SnackbarAction(
        label = UiMessage.Resource(R.string.error_retry),
        argument = error
    )
)

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val character: ObserveCharacter,
    private val characterDetails: ObserveCharacterDetails,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    private val characterParams = handle.get<Long>(CHARACTER_ID_KEY) ?: -1

    val state = combine(character.flow, characterDetails.asyncFlow, ::CharacterDetailViewState)
        .stateInDefault(viewModelScope, CharacterDetailViewState.EMPTY)

    init {
        load()
        observeDetailErrors()
    }

    private fun load(forceRefresh: Boolean = false) {
        character(characterParams)
        if (forceRefresh)
            characterDetails(GetCharacterDetails.Params(-1))
        characterDetails(GetCharacterDetails.Params(characterParams, forceRefresh))
    }

    private fun observeDetailErrors() = viewModelScope.launch {
        characterDetails.asyncFlow.collect {
            if (it is Fail) {
                val failedToLoadMessage = DetailsFailedToLoadMessage(it.error)
                snackbarManager.addMessage(failedToLoadMessage)
                if (snackbarManager.observeMessageAction(failedToLoadMessage) != null) {
                    refresh()
                }
            }
        }
    }

    fun refresh() = load(true)
}
