/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tm.alashow.base.ui.SnackbarManager
import tm.alashow.base.util.extensions.stateInDefault
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.observers.character.ObservePagedCharacters

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val observePagedCharacters: ObservePagedCharacters,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    private val defaultParams = CharactersParams()
    private val characterParamsState = MutableStateFlow(defaultParams)

    val charactersPager = observePagedCharacters.flow

    val state = snackbarManager.errors.map(::CharactersViewState)
        .stateInDefault(viewModelScope, CharactersViewState.EMPTY)

    init {
        viewModelScope.launch {
            characterParamsState.collectLatest(::load)
        }
        viewModelScope.launch {
            observePagedCharacters.errors().collectLatest(snackbarManager::addError)
        }
    }

    private fun load(params: CharactersParams = defaultParams) {
        val observeParams = ObservePagedCharacters.Params(params)
        observePagedCharacters(observeParams)
    }

    fun clearError() = viewModelScope.launch {
        snackbarManager.removeCurrentError()
    }
}
