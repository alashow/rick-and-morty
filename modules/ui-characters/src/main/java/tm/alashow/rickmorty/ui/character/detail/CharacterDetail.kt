/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.ui.character.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ui.LocalScaffoldPadding
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import tm.alashow.common.compose.rememberFlowWithLifecycle
import tm.alashow.domain.extensions.blankIfZero
import tm.alashow.navigation.LocalNavigator
import tm.alashow.navigation.Navigator
import tm.alashow.navigation.screens.LeafScreen
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.entities.CharacterId
import tm.alashow.rickmorty.domain.entities.Location
import tm.alashow.rickmorty.ui.character.components.CharacterColumn
import tm.alashow.rickmorty.ui.character.components.CharacterStatusDot
import tm.alashow.rickmorty.ui.character.R
import tm.alashow.ui.components.*
import tm.alashow.ui.theme.AppTheme

@Composable
fun CharacterDetail() {
    CharacterDetail(viewModel = hiltViewModel())
}

@Composable
private fun CharacterDetail(
    viewModel: CharacterDetailViewModel,
    navigator: Navigator = LocalNavigator.current,
) {
    val viewState by rememberFlowWithLifecycle(viewModel.state)
    Scaffold(
        topBar = {
            AppTopBar(
                title = viewState.character?.name ?: "",
                navigationIcon = {
                    AppBarNavigationIcon(onClick = navigator::goBack)
                }
            )
        },
    ) {
        CharacterDetailSwipeRefresh(
            onRefresh = viewModel::refresh,
        ) {
            if (viewState.isLoaded) {
                val character = viewState.characterDetails() ?: viewState.character
                if (character != null)
                    CharactersDetailContent(
                        character = character,
                        isDetailsLoading = viewState.isDetailsLoading
                    )
            } else FullScreenLoading()
        }
    }
}

@Composable
private fun CharacterDetailSwipeRefresh(
    onRefresh: () -> Unit,
    isRefreshing: Boolean = false,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = onRefresh,
        indicatorPadding = LocalScaffoldPadding.current,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true
            )
        },
        content = content,
    )
}

@Composable
private fun CharactersDetailContent(
    character: Character,
    modifier: Modifier = Modifier,
    isDetailsLoading: Boolean = false,
    contentPadding: PaddingValues = LocalScaffoldPadding.current,
) {
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        contentPadding = contentPadding,
    ) {
        item("header") {
            CharacterDetailsHeader(character)
        }
        if (!character.location.isUnknown)
            item("last_seen_location") {
                CharacterLocationSection(
                    label = stringResource(R.string.character_location),
                    residentsLabel = stringResource(R.string.character_location_residents),
                    currentCharacterId = character.id,
                    location = character.location,
                    isDetailsLoading = isDetailsLoading,
                )
            }
        if (character.location != character.origin && !character.origin.isUnknown)
            item("origin") {
                CharacterLocationSection(
                    label = stringResource(R.string.character_origin),
                    residentsLabel = stringResource(R.string.character_origin_residents),
                    currentCharacterId = character.id,
                    location = character.origin,
                    isDetailsLoading = isDetailsLoading,
                )
            }
        item {
            Spacer(Modifier.padding(AppTheme.specs.paddingLarge))
        }
    }
}

@Composable
private fun CharacterDetailsHeader(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.specs.padding,
                vertical = AppTheme.specs.paddingSmall,
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppTheme.specs.padding),
            contentAlignment = Alignment.Center
        ) {
            CoverImage(
                character.imageUrl,
                shape = CircleShape,
                size = 250.dp,
            )
        }

        CharacterDetailRow(
            label = stringResource(R.string.character_status),
            value = character.status,
            separator = {
                CharacterStatusDot(character)
            }
        )
        CharacterDetailRow(
            label = stringResource(R.string.character_species),
            value = character.species
        )
        CharacterDetailRow(
            label = stringResource(R.string.character_type),
            value = character.type
        )
    }
}

@Composable
private fun CharacterLocationSection(
    label: String,
    residentsLabel: String,
    currentCharacterId: CharacterId,
    location: Location,
    isDetailsLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = AppTheme.specs.padding)) {
        CharacterDetailLabel(label)
        CharacterLocation(
            location = location,
            isDetailsLoading = isDetailsLoading
        )
    }
    CharacterLocationResidents(
        currentCharacterId = currentCharacterId,
        location = location,
        label = residentsLabel,
    )
}

@Composable
private fun CharacterLocation(
    location: Location,
    isDetailsLoading: Boolean
) {
    with(location) {
        CharacterDetailRow(
            label = stringResource(R.string.character_location_name),
            value = name
        )
        CharacterDetailRow(
            label = stringResource(R.string.character_location_type),
            value = type,
            isDetailsLoading = isDetailsLoading,
        )
        CharacterDetailRow(
            label = stringResource(R.string.character_location_dimension),
            value = dimension,
            isDetailsLoading = isDetailsLoading,
        )
        CharacterDetailRow(
            label = stringResource(R.string.character_location_numberOfResidents),
            value = residents.size.blankIfZero(),
            isDetailsLoading = isDetailsLoading,
        )
    }
}

@Composable
private fun CharacterLocationResidents(
    currentCharacterId: CharacterId,
    location: Location,
    label: String,
    navigator: Navigator = LocalNavigator.current,
) {
    val residents = location.residentsCharacters.filterNot { it.id == currentCharacterId }
    if (residents.isNotEmpty())
        CharacterDetailLabel(
            label = label,
            Modifier.padding(start = AppTheme.specs.padding)
        )
    LazyRow(contentPadding = PaddingValues(horizontal = AppTheme.specs.paddingSmall)) {
        items(residents, key = { it.id }) {
            CharacterColumn(
                character = it,
                onClick = { navigator.navigate(LeafScreen.CharacterDetails.buildRoute(it.id)) },
            )
        }
    }
}

@Composable
private fun CharacterDetailLabel(label: String, modifier: Modifier = Modifier) {
    Text(
        text = label,
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(
            top = AppTheme.specs.padding,
            bottom = AppTheme.specs.paddingSmall
        )
    )
}

@Composable
private fun CharacterDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isDetailsLoading: Boolean = false,
    separator: @Composable RowScope.() -> Unit = {},
    labelStyle: TextStyle = MaterialTheme.typography.body1,
    valueStyle: TextStyle = MaterialTheme.typography.body1,
) {
    val hasValue = value.isNotBlank()
    val loadingModifier = Modifier.placeholder(
        visible = !hasValue && isDetailsLoading,
        highlight = shimmer(),
    )
    if (hasValue || isDetailsLoading)
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(vertical = AppTheme.specs.paddingTiny)
                .then(loadingModifier)
        ) {
            Text(
                text = "$label:",
                style = labelStyle,
                fontWeight = FontWeight.Bold
            )
            separator()
            Text(
                text = value.replaceFirstChar { it.uppercase() },
                style = valueStyle
            )
        }
}
