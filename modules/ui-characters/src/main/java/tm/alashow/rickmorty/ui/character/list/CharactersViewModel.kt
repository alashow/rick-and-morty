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
import kotlinx.coroutines.launch
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.observers.character.ObservePagedCharacters

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val observePagedCharacters: ObservePagedCharacters,
) : ViewModel() {

    private val defaultParams = CharactersParams()
    private val characterParamsState = MutableStateFlow(defaultParams)

    val charactersPager = observePagedCharacters.flow

    init {
        viewModelScope.launch {
            characterParamsState.collectLatest(::load)
        }
    }

    private fun load(params: CharactersParams = defaultParams) {
        val observeParams = ObservePagedCharacters.Params(params)
        observePagedCharacters(observeParams)
    }
}
