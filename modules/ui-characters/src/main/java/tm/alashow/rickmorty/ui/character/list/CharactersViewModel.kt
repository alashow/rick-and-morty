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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import tm.alashow.base.ui.SnackbarManager
import tm.alashow.base.util.extensions.getStateFlow
import tm.alashow.base.util.extensions.stateInDefault
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.observers.character.ObserveCharactersFilterOptions
import tm.alashow.rickmorty.data.observers.character.ObservePagedCharacters

@HiltViewModel
class CharactersViewModel @Inject constructor(
    handle: SavedStateHandle,
    observeCharactersFilterOptions: ObserveCharactersFilterOptions,
    private val observePagedCharacters: ObservePagedCharacters,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    private val defaultParams = CharactersParams()
    private val characterParamsState = MutableStateFlow(defaultParams)
    private val charactersFiltersState = handle.getStateFlow("filters", viewModelScope, defaultParams.filters)

    val charactersPager = observePagedCharacters.flow

    val state = combine(observeCharactersFilterOptions.flow, charactersFiltersState, snackbarManager.errors, ::CharactersViewState)
        .stateInDefault(viewModelScope, CharactersViewState.EMPTY)

    init {
        observeCharactersFilterOptions(Unit)
        viewModelScope.launch {
            charactersFiltersState.collectLatest {
                characterParamsState.value = characterParamsState.value.copy(filters = it)
            }
        }
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

    fun setStatusFilter(status: String?) {
        charactersFiltersState.value = charactersFiltersState.value.copy(
            status = status.takeIf { it != charactersFiltersState.value.status }
        )
    }

    fun setSpeciesFilter(species: String?) {
        charactersFiltersState.value = charactersFiltersState.value.copy(
            species = species.takeIf { it != charactersFiltersState.value.species }
        )
    }

    fun setTypeFilter(type: String?) {
        charactersFiltersState.value = charactersFiltersState.value.copy(
            type = type.takeIf { it != charactersFiltersState.value.type }
        )
    }

    fun setGenderFilter(gender: String?) {
        charactersFiltersState.value = charactersFiltersState.value.copy(
            gender = gender.takeIf { it != charactersFiltersState.value.gender }
        )
    }

    fun setOriginFilter(origin: String?) {
        charactersFiltersState.value = charactersFiltersState.value.copy(
            origin = origin.takeIf { it != charactersFiltersState.value.origin }
        )
    }

    fun setLocationFilter(location: String?) {
        charactersFiltersState.value = charactersFiltersState.value.copy(
            location = location.takeIf { it != charactersFiltersState.value.location }
        )
    }

    fun clearFilters() {
        charactersFiltersState.value = defaultParams.filters
    }

    fun clearError() = viewModelScope.launch {
        snackbarManager.removeCurrentError()
    }
}
