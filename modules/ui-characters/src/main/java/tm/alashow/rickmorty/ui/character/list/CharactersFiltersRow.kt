/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.list

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tm.alashow.domain.extensions.orBlank
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.ui.character.R
import tm.alashow.rickmorty.ui.character.components.CharacterStatusDot
import tm.alashow.ui.components.SelectableDropdownMenu
import tm.alashow.ui.theme.AppTheme
import tm.alashow.ui.theme.translucentSurface

@Composable
internal fun CharactersFiltersRow(
    filterOptions: CharactersParams.FilterOptions,
    filters: CharactersParams.Filters,
    onStatusSelect: (String?) -> Unit,
    onSpeciesSelect: (String?) -> Unit,
    onTypeSelect: (String?) -> Unit,
    onGenderSelect: (String?) -> Unit,
    onOriginSelect: (String?) -> Unit,
    onLocationSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .translucentSurface(),
        contentPadding = PaddingValues(horizontal = AppTheme.specs.paddingSmall)
    ) {
        charactersSelectableDropdownFilter(
            labelRes = R.string.characters_filters_status,
            selections = filterOptions.statusOptions,
            selectedItem = filters.status,
            onSelect = onStatusSelect,
            itemPrefixMapper = {
                CharacterStatusDot(
                    isAlive = it?.lowercase() == "alive",
                    isDead = it?.lowercase() == "dead",
                    modifier = Modifier.padding(end = AppTheme.specs.paddingSmall)
                )
            }
        )
        charactersSelectableDropdownFilter(
            labelRes = R.string.characters_filters_species,
            selections = filterOptions.speciesOptions,
            selectedItem = filters.species,
            onSelect = onSpeciesSelect,
        )
        charactersSelectableDropdownFilter(
            labelRes = R.string.characters_filters_type,
            selections = filterOptions.typeOptions,
            selectedItem = filters.type,
            onSelect = onTypeSelect,
        )
        charactersSelectableDropdownFilter(
            labelRes = R.string.characters_filters_gender,
            selections = filterOptions.genderOptions,
            selectedItem = filters.gender,
            onSelect = onGenderSelect,
        )
        charactersSelectableDropdownFilter(
            labelRes = R.string.characters_filters_origin,
            selections = filterOptions.originOptions,
            selectedItem = filters.origin,
            onSelect = onOriginSelect,
        )
        charactersSelectableDropdownFilter(
            labelRes = R.string.characters_filters_location,
            selections = filterOptions.locationOptions,
            selectedItem = filters.location,
            onSelect = onLocationSelect,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.charactersSelectableDropdownFilter(
    @StringRes labelRes: Int,
    selections: Set<String>,
    selectedItem: String?,
    onSelect: (String?) -> Unit,
    modifier: Modifier = Modifier,
    itemPrefixMapper: @Composable (RowScope.(String?) -> Unit)? = null,
) {
    item {
        val emptyOptionText = stringResource(R.string.characters_filters_emptyOption)
        SelectableDropdownMenu(
            dropdownText = stringResource(labelRes),
            items = selections.toList(),
            selectedItem = selectedItem,
            onItemSelect = onSelect,
            selectedDropdownIconColor = MaterialTheme.colors.secondary,
            itemPrefixMapper = itemPrefixMapper,
            itemLabelMapper = {
                it.orBlank()
                    .ifBlank { emptyOptionText }
                    .replaceFirstChar { c -> c.uppercase() }
            },
            maxDropdownHeight = 400.dp,
            modifier = modifier
        )
    }
}
